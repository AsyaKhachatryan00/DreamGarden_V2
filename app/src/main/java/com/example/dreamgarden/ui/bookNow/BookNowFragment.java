package com.example.dreamgarden.ui.bookNow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.dreamgarden.Common.Common;
import com.example.dreamgarden.EventBus.GetTablesClick;
import com.example.dreamgarden.Models.BookNow;
import com.example.dreamgarden.R;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

public class BookNowFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    EditText name, phone, date, time, count, event, other;

    public static BookNowFragment newInstance() {
        return new BookNowFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_now_fragment, container, false);

         name = view.findViewById(R.id.book_name);
         phone = view.findViewById(R.id.book_phone);
         date = view.findViewById(R.id.date);
         time = view.findViewById(R.id.time);
         count = view.findViewById(R.id.guests_count);
         event = view.findViewById(R.id.event);
         other = view.findViewById(R.id.other);
        Spinner spinner = view.findViewById(R.id.spinner);
        Button confirm = view.findViewById(R.id.book_now);
        Button tables = view.findViewById(R.id.table);
        tables.setOnClickListener(v -> EventBus.getDefault().postSticky(new GetTablesClick(true)));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        name.setText(Common.currentUser.getFName());
        phone.setText(Common.currentUser.getPNumber());

        confirm.setOnClickListener(v -> {
            bookNow(name.getText().toString(),phone.getText().toString(),
                    date.getText().toString(),time.getText().toString(),
                    count.getText().toString(),event.getText().toString(),
                    other.getText().toString(), spinner.getSelectedItem().toString());
        });

        return view;
    }

    private void bookNow(String name, String phone, String date, String time, String count, String event, String other, String tables) {
        BookNow bookNow = new BookNow();
        bookNow.setUserId(Common.currentUser.getUid());
        bookNow.setUserName(name); bookNow.setUserPhone(phone);
        bookNow.setDate(date); bookNow.setTime(time);
        bookNow.setGuestCount(count);
        bookNow.setEvent(event); bookNow.setOther(other);
        bookNow.setTableNumber(tables);
        writeBookToFirebase(bookNow);
    }

    private void writeBookToFirebase(BookNow bookNow) {
        FirebaseDatabase.getInstance().getReference(Common.BOOK_NOW_REF)
                .child(Common.bookNumber())
                .setValue(bookNow)
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
        Toast.makeText(getContext(), "Ամրագրումը հաստատված է", Toast.LENGTH_SHORT).show();
        name.setText(""); phone.setText("");
        date.setText(""); time.setText("");
        count.setText(""); event.setText("");
        other.setText("");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}