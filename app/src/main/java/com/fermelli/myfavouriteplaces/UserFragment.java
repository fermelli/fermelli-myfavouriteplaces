package com.fermelli.myfavouriteplaces;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        TextView textViewName = (TextView) view.findViewById(R.id.textViewName);
        TextView textViewEmail = (TextView) view.findViewById(R.id.textViewEmail);
        TextView textViewProvider = (TextView) view.findViewById(R.id.textViewProvider);
        TextView textViewPhone = (TextView) view.findViewById(R.id.textViewPhone);
        String phone = user.getPhoneNumber() != "" ? user.getPhoneNumber() : "-";
        textViewName.setText(user.getDisplayName());
        textViewEmail.setText(user.getEmail());
        textViewProvider.setText(user.getProviderId());
        textViewPhone.setText(phone);

        RequestQueue queueRequest = Volley.newRequestQueue(getActivity().getApplicationContext());
        ImageLoader imageLoader = new ImageLoader(queueRequest, new ImageLoader.ImageCache() {

            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(10);

            @Nullable
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });

        Uri urlImage = user.getPhotoUrl();

        if (urlImage != null) {
            NetworkImageView networkImageView = (NetworkImageView) view.findViewById(R.id.image);
            networkImageView.setImageUrl(urlImage.toString(), imageLoader);
        }
        return view;
    }
}