package com.droidblossom.archive.presentation.ui.camera

import android.content.Context
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ItemCapsuleSkinBinding
import com.droidblossom.archive.domain.model.capsule.CapsuleAnchor
import com.droidblossom.archive.presentation.ui.home.dialog.CapsulePreviewDialogFragment
import com.droidblossom.archive.util.FragmentManagerProvider
import com.google.ar.sceneform.rendering.ViewAttachmentManager
import com.google.ar.sceneform.rendering.ViewRenderable
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.node.ViewNode


class ARContentNode(
    val arscene: ARSceneView,
    val viewAttManager: ViewAttachmentManager,
    val fragmentManagerProvider: FragmentManagerProvider,
    val capsule: CapsuleAnchor,
    val layoutInflater: LayoutInflater,
    val context: Context,
    val onLoaded: (node: ViewNode) -> Unit
) {

    init {
        renderContent()
    }

    private fun renderContent() {

        //스킨 이미지 추가
        val capsuleSkin = ItemCapsuleSkinBinding.inflate(layoutInflater)

        Glide.with(context)
            .load(capsule.skinUrl)
            .placeholder(R.drawable.sample_skin)
            .error(R.drawable.sample_skin)
            .fallback(R.drawable.sample_skin)
            .into(capsuleSkin.capsuleSkin)

        ViewRenderable.builder()
            .setView(arscene.context, capsuleSkin.root)
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
                        val sheet = CapsulePreviewDialogFragment.newInstance("-1",capsule.id.toString(), capsule.capsuleType.toString(), true)
                        sheet.show(fragmentManagerProvider.provideFragmentManager(), "CapsulePreviewDialog")
                        false
                    }
                }
                onLoaded(viewNode)
            }
    }
}