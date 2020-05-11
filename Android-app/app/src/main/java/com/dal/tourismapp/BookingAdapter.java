package com.dal.tourismapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder> {
    private ArrayList<String> mPlace;
    private ArrayList<String> mCity;
    private ArrayList<String> mTicket_id;
    private ArrayList<String> mPeople;
    private ArrayList<String> mSource;
    private ArrayList<String> mDate_booked;
    private ArrayList<String> mDate_travel;
    private ArrayList<String> mTotal;
    private Context mcontext;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView place;
        public TextView city;
        public TextView ticket_id;
        public TextView people;
        public TextView date_booked;
        public TextView date_travel;
        public TextView total;
        public TextView source;

        public MyViewHolder(View v) {
            super(v);
            place = v.findViewById(R.id.place);
            city = v.findViewById(R.id.city);
            ticket_id = v.findViewById(R.id.ticket);
            people = v.findViewById(R.id.passenger);
            source = v.findViewById(R.id.source);
            date_booked = v.findViewById(R.id.date_booked);
            date_travel = v.findViewById(R.id.travel_Date);
            total = v.findViewById(R.id.total);
        }
    }
    public BookingAdapter(ArrayList<String> Place, ArrayList<String> city, ArrayList<String> ticket_id, ArrayList<String> people, ArrayList<String> source, ArrayList<String> date_booked, ArrayList<String> date_travel, ArrayList<String> total, Context context) {

        mPlace = Place;
        mCity = city;
        mTicket_id = ticket_id;
        mPeople = people;
        mSource = source;
        mDate_booked = date_booked;
        mDate_travel = date_travel;
        mTotal= total;
    }


    @Override
    public void onBindViewHolder(@NonNull BookingAdapter.MyViewHolder holder, final int position) {

        holder.place.setText(mPlace.get(position).toUpperCase());
        holder.city.setText(mCity.get(position));
        holder.source.setText("Source: " + mSource.get(position));
        holder.ticket_id.setText("Ticket ID: " + mTicket_id.get(position));
        holder.people.setText("No. of Passenger: " + mPeople.get(position));
        holder.date_booked.setText("Date of Booking: " + mDate_booked.get(position));
        holder.date_travel.setText("Date of Travel: " + mDate_travel.get(position));
        holder.total.setText("Ticket Price: $" + mTotal.get(position));
    }
    //creating my views
    @Override
    public  BookingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //create a new view
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_card, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public int getItemCount() {
        return mCity.size();
    }
}