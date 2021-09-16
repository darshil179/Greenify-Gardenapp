package com.example.elevateinterior.activities;

import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.elevateinterior.R;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;

import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class ArActivity extends AppCompatActivity {
    private ArFragment fragment;
    private Uri selectedObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

        fragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);

        InitializeGallery();

        fragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if(plane.getType() != Plane.Type.HORIZONTAL_UPWARD_FACING){
                        return;
                    }
                    Anchor anchor = hitResult.createAnchor();
                    placeObject(fragment, anchor, selectedObject);
                }
        );
    }

    private void InitializeGallery() {
        LinearLayout gallery = findViewById(R.id.gallery_layout);

        ImageView couch = new ImageView(this);
        couch.setImageResource(R.drawable.couch_thumb);
        couch.setContentDescription("couch");
        couch.setOnClickListener(view -> {selectedObject = Uri.parse("couch.sfb");});
        gallery.addView(couch);

        ImageView computertable = new ImageView(this);
        computertable.setImageResource(R.drawable.computertable);
        computertable.setContentDescription("computertable");
        computertable.setOnClickListener(view -> {selectedObject = Uri.parse("computertable.sfb");});
        gallery.addView(computertable);

        ImageView drafttable = new ImageView(this);
        drafttable.setImageResource(R.drawable.drafttable);
        drafttable.setContentDescription("drafttable");
        drafttable.setOnClickListener(view -> {selectedObject = Uri.parse("drafttable.sfb");});
        gallery.addView(drafttable);

        ImageView window = new ImageView(this);
        window.setImageResource(R.drawable.window);
        window.setContentDescription("window");
        window.setOnClickListener(view -> {selectedObject = Uri.parse("window.sfb");});
        gallery.addView(window);

        ImageView tvtable = new ImageView(this);
        tvtable.setImageResource(R.drawable.tvtable);
        tvtable.setContentDescription("tvtable");
        tvtable.setOnClickListener(view -> {selectedObject = Uri.parse("tvtable.sfb");});
        gallery.addView(tvtable);
    }

    private void placeObject(ArFragment fragment, Anchor anchor, Uri model){
        ModelRenderable.builder()
                .setSource(fragment.getContext(),model)
                .build()
                .thenAccept(renderable -> addNodeToScene(fragment, anchor, renderable))
                .exceptionally((throwable -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(throwable.getMessage())
                            .setTitle("Error1");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return null;
                }));
    }

    private void addNodeToScene(ArFragment fragment, Anchor anchor, Renderable renderable){
        AnchorNode anchorNode = new AnchorNode(anchor);
        TransformableNode node = new TransformableNode(fragment.getTransformationSystem());
        node.setRenderable(renderable);
        node.setParent(anchorNode);
        fragment.getArSceneView().getScene().addChild(anchorNode);
        node.select();
    }
}