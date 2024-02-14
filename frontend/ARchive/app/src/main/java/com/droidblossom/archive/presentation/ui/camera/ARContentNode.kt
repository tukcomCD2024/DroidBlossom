package com.droidblossom.archive.presentation.ui.camera

import android.content.Context
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ItemCapsuleSkinBinding
import com.droidblossom.archive.domain.model.common.CapsuleMarker
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
    val capsule: CapsuleMarker,
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
            .load("https://blog.kakaocdn.net/dn/dq8OFz/btq4AuchShs/PqmCKZK20PEYKtUssYe0U0/img.gif")
            .placeholder(R.drawable.app_symbol)
            .error(R.drawable.app_symbol)
            .fallback(R.drawable.app_symbol)
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
                        val sheet = CapsulePreviewDialogFragment.newInstance(capsule.id.toString(), capsule.capsuleType.toString())
                        sheet.show(fragmentManagerProvider.provideFragmentManager(), "CapsulePreviewDialog")
                        false
                    }
                }
                onLoaded(viewNode)
            }
    }
}