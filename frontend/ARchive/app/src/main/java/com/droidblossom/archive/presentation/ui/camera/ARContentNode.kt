package com.droidblossom.archive.presentation.ui.camera

import android.util.Log
import com.droidblossom.archive.R
import com.droidblossom.archive.domain.model.common.CapsuleMarker
import com.google.ar.sceneform.rendering.ViewAttachmentManager
import com.google.ar.sceneform.rendering.ViewRenderable
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.node.ViewNode

class ARContentNode(
    val arscene: ARSceneView,
    val viewAttManager: ViewAttachmentManager,
    val viewModel : CameraViewModel,
    val capsule : CapsuleMarker,
    val onLoaded: (node: ViewNode) -> Unit
) {

    init {
        renderContent()
    }

    private fun renderContent() {
        ViewRenderable.builder()
            .setView(arscene.context, R.layout.capsule_skin)
            .build(arscene.engine).thenAccept { r: ViewRenderable? ->
                val viewNode = ViewNode(
                    engine = arscene.engine,
                    modelLoader = arscene.modelLoader,
                    viewAttachmentManager = viewAttManager
                ).apply {
                    if (r != null) {
                        setRenderable(r)
                        isEditable = true
                    }
                    onSingleTapConfirmed = {
                        viewModel.cameraEvent(CameraViewModel.CameraEvent.ShowCapsulePreviewDialog(capsule.id.toString(), capsule.capsuleType.toString()))
                        false
                    }
                }
                onLoaded(viewNode)
            }
    }
}