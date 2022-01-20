package com.parthdesai1208.myar

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.parthdesai1208.myar.databinding.ActivityAractivityBinding
import com.parthdesai1208.myar.databinding.ActivityMainBinding

class ARActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAractivityBinding

    lateinit var fragment: ArFragment
    private var whichObject = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAractivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        fragment = supportFragmentManager
            .findFragmentById(R.id.sceneform_fragment)
                as ArFragment

        fragment.setOnTapArPlaneListener { hitResult, _, _ ->

            when (whichObject) {
                "sphere" -> {
                    binding.btnAddSphere.text = "Bounce"
                    MaterialFactory.makeOpaqueWithColor(this,
                        com.google.ar.sceneform.rendering.Color(Color.RED))
                        .thenAccept { material ->
                            addNodeToScene(fragment, hitResult.createAnchor(),
                                ShapeFactory.makeSphere(0.05f, Vector3(0.10f, 0.0f, 0.00f), material))
                        }

                    MaterialFactory.makeOpaqueWithColor(this,
                        com.google.ar.sceneform.rendering.Color(Color.RED))
                        .thenAccept { material ->
                            addNodeToScene(fragment, hitResult.createAnchor(),
                                ShapeFactory.makeSphere(0.05f, Vector3(0.40f, 0.0f, 0.00f), material))
                        }
                }
                "box" -> {
                    binding.btnAddBox.visibility = View.GONE
                    MaterialFactory.makeOpaqueWithColor(this,
                        com.google.ar.sceneform.rendering.Color(Color.BLUE))
                        .thenAccept { material ->
                            addNodeToScene(fragment, hitResult.createAnchor(),
                                ShapeFactory.makeCube(Vector3(0.05f, 0.05f, 0.05f), Vector3(0.25f, 0.0f, 0.0f), material))
                        }
                }
            }

        }


        binding.btnAddSphere.setOnClickListener {
            if(binding.btnAddSphere.text == getString(R.string.add_sphere)){
                whichObject = "sphere"
                Snackbar.make(binding.root, "Now tap on the surface to add", Snackbar.LENGTH_LONG)
                    .setAction("ok") {}.show()
            }else if(binding.btnAddSphere.text == "Bounce"){
                Snackbar.make(binding.root, "animate sphere", Snackbar.LENGTH_LONG).show()
            }
        }

        binding.btnAddBox.setOnClickListener {
            whichObject = "box"
            Snackbar.make(binding.root, "Now tap on the surface to add", Snackbar.LENGTH_LONG)
                .setAction("ok") {}.show()
        }

    }


    private fun addNodeToScene(fragment: ArFragment, anchor: Anchor, modelObject: ModelRenderable) {

        val anchorNode = AnchorNode(anchor)

        TransformableNode(fragment.transformationSystem).apply {
            renderable = modelObject
            setParent(anchorNode)
            select()
        }

        fragment.arSceneView.scene.addChild(anchorNode)
    }
}