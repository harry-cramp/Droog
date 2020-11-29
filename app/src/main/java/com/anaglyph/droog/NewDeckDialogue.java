package com.anaglyph.droog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

public class NewDeckDialogue extends AppCompatDialogFragment {

    private MainActivity main;

    public NewDeckDialogue(MainActivity main) {
        this.main = main;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.new_deck_dialogue, null);

        builder.setView(view);
        builder.setNegativeButton("cancel", null);
        builder.setPositiveButton("create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextView deckNameBox = view.findViewById(R.id.createNewDeckTitleBox);

                String deckName = deckNameBox.getText().toString();
                main.addNewDeck(deckName);
            }
        });

        return builder.create();
    }

}
