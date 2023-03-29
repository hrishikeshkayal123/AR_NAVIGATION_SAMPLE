package com.hrishikeshkayal123.ar.navigation

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.ar.core.Anchor
import com.google.ar.core.HitResult
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.hrishikeshkayal123.ar.navigation.databinding.ActivityAractivityBinding
import java.io.File


class ARActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAractivityBinding

    private lateinit var fragment: ArFragment
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
                    addSphere(hitResult)
                }
                "box" -> {
                    //addBox(hitResult)
                }
            }

        }


        binding.btnAddSphere.setOnClickListener {
            whichObject = "sphere"
            Snackbar.make(binding.sceneformFragment, "Now tap on the surface to add", Snackbar.LENGTH_LONG)
                .setAnchorView(binding.btnAddSphere)
                .setAction("ok") {}.show()
        }

        binding.btnAddBox.setOnClickListener {
            whichObject = "box"
            Snackbar.make(binding.sceneformFragment, "Now tap on the surface to add", Snackbar.LENGTH_LONG)
                .setAnchorView(binding.btnAddBox)
                .setAction("ok") {}.show()
        }


    }

    /*private fun addBox(anchor: Anchor) {
        MaterialFactory.makeOpaqueWithColor(
            this,
            com.google.ar.sceneform.rendering.Color(Color.BLUE)
        )
            .thenAccept { material ->
                addNodeToScene(
                    fragment, anchor,
                    ShapeFactory.makeCube(
                        Vector3(0.05f, 0.05f, 0.05f),
                        Vector3(0.25f, 0.0f, 0.0f),
                        material
                    )
                )
            }
    }*/

    private fun addSphere(hitResult: HitResult) {

        val anchor: Anchor = hitResult.createAnchor()
        File("")
        val GLTF_ASSET = "file:///android_asset/scene.gltf"
        ModelRenderable.builder().setSource(this, RenderableSource.builder().setScale(0.5f).setSource(
            this,
            Uri.parse(GLTF_ASSET),
            RenderableSource.SourceType.GLTF2).build())
            .setRegistryId(GLTF_ASSET)
            .build().thenAccept {
            addNodeToScene(
                fragment, anchor,
                it
            )
        }

//        MaterialFactory.makeOpaqueWithColor(
//            this,
//            com.google.ar.sceneform.rendering.Color(Color.RED)
//        )
//            .thenAccept { material ->
//                (1..10).forEach{
//
//                    addNodeToScene(
//                        fragment, anchor,
//                        ShapeFactory.makeSphere(0.05f, Vector3(0.0f, 0.0f, -(it.toFloat()*0.25f)), material)
//                    )
//                }
//
//            }

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