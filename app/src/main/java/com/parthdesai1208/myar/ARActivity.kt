package com.parthdesai1208.myar

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.ar.core.Anchor
import com.google.ar.core.Plane
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.parthdesai1208.myar.databinding.ActivityAractivityBinding
import android.view.MotionEvent

import com.google.ar.core.HitResult
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3Evaluator

import android.animation.ObjectAnimator

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
//                    AppPref.isUserRenderObjectPreviously = true
                    if(binding.btnAddSphere.visibility == View.GONE) return@setOnTapArPlaneListener
                    binding.btnAddSphere.visibility = View.GONE
                    addSphere(hitResult.createAnchor())
                }
                "box" -> {
                    //AppPref.isUserRenderObjectPreviously = true
                    if(binding.btnAddBox.visibility == View.GONE) return@setOnTapArPlaneListener
                    binding.btnAddBox.visibility = View.GONE
                    addBox(hitResult.createAnchor())
                }
            }

        }


        /*fragment.arSceneView.scene.addOnUpdateListener {
            if (AppPref.isUserRenderObjectPreviously) {
                if (isModelPlaced) return@addOnUpdateListener

                val frame = fragment.arSceneView.arFrame
                val collections = frame?.getUpdatedTrackables(Plane::class.java)
                if (collections != null) {
                    for (collection in collections) {
                        if (collection.trackingState == TrackingState.TRACKING) {
                            val anchor = collection.createAnchor(collection.centerPose)
                            if (AppPref.isUserRenderedSphere) {
                                sphereAnchor = anchor
                                addSphere(anchor)
                            }
                            if (AppPref.isUserRenderedBox) {
                                boxAnchor = anchor
                                addBox(anchor)
                            }
                            isModelPlaced = true
                            break
                        }
                    }
                }
            }
        }*/

        binding.btnAddSphere.setOnClickListener {
            whichObject = "sphere"
            Snackbar.make(binding.root, "Now tap on the surface to add", Snackbar.LENGTH_LONG)
                .setAnchorView(binding.btnAddSphere)
                .setAction("ok") {}.show()
        }

        binding.btnAddBox.setOnClickListener {
            whichObject = "box"
            Snackbar.make(binding.root, "Now tap on the surface to add", Snackbar.LENGTH_LONG)
                .setAnchorView(binding.btnAddBox)
                .setAction("ok") {}.show()
        }

        /*binding.btnBounce.setOnClickListener {
            val nodeAnimator: ObjectAnimator = ObjectAnimator.ofObject(
                sphereAnchor,
                "localPosition",
                Vector3Evaluator(),
                Vector3.zero(), Vector3(0f, 1f, 0f)
            )
            nodeAnimator.start()
        }*/

    }

    /*override fun onBackPressed() {
        super.onBackPressed()
        if(binding.btnAddSphere.visibility == View.GONE){
            AppPref.isUserRenderedSphere = true
        }
        if(binding.btnAddBox.visibility == View.GONE){
            AppPref.isUserRenderedBox = true
        }
    }*/

    private fun addBox(anchor: Anchor) {
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
        /*if(AppPref.isUserRenderedSphere){
            binding.grp.visibility = View.VISIBLE
        }*/
    }

    private fun addSphere(anchor: Anchor) {
        MaterialFactory.makeOpaqueWithColor(
            this,
            com.google.ar.sceneform.rendering.Color(Color.RED)
        )
            .thenAccept { material ->
                addNodeToScene(
                    fragment, anchor,
                    ShapeFactory.makeSphere(0.05f, Vector3(0.10f, 0.0f, 0.00f), material)
                )
            }

        MaterialFactory.makeOpaqueWithColor(
            this,
            com.google.ar.sceneform.rendering.Color(Color.RED)
        )
            .thenAccept { material ->
                addNodeToScene(
                    fragment, anchor,
                    ShapeFactory.makeSphere(0.05f, Vector3(0.40f, 0.0f, 0.00f), material)
                )
            }

        /*if(AppPref.isUserRenderedBox){
            binding.grp.visibility = View.VISIBLE
        }*/
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