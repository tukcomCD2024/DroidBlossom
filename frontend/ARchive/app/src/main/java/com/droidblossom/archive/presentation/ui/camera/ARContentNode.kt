package com.droidblossom.archive.presentation.ui.camera

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.droidblossom.archive.R
import com.droidblossom.archive.databinding.ItemCapsuleSkinBinding
import com.droidblossom.archive.domain.model.capsule.CapsuleAnchor
import com.droidblossom.archive.presentation.ui.capsulepreview.CapsulePreviewDialogFragment
import com.droidblossom.archive.util.FlipTransformation
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
            .placeholder(R.drawable.base_skin)
            .override(350, 350)
            .error(R.drawable.base_skin)
            .fallback(R.drawable.base_skin)
            .transform(FlipTransformation())
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
                        val existingDialog = fragmentManagerProvider.provideFragmentManager()
                            .findFragmentByTag(CapsulePreviewDialogFragment.TAG) as DialogFragment?
                        if (existingDialog == null) {
                            val dialog = CapsulePreviewDialogFragment.newInstance(
                                "-1",
                                capsule.id.toString(),
                                capsule.capsuleType.toString(),
                                true
                            )
                            dialog.show(
                                fragmentManagerProvider.provideFragmentManager(),
                                CapsulePreviewDialogFragment.TAG
                            )
                        }
                        true
                    }
                }
                onLoaded(viewNode)
            }
    }
}