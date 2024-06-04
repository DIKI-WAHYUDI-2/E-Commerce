package com.example.e_commerce.fragment.profile;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_commerce.Administrator;
import com.example.e_commerce.LoginActivity;
import com.example.e_commerce.R;
import com.example.e_commerce.data.response.Session;
import com.example.e_commerce.data.response.SessionResponse;
import com.example.e_commerce.data.response.User;
import com.example.e_commerce.data.retrofit.ApiConfig;
import com.example.e_commerce.data.retrofit.ApiConfigAuthenticated;
import com.example.e_commerce.data.retrofit.ApiService;
import com.example.e_commerce.databinding.FragmentProfileBinding;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private ActivityResultLauncher<String> requestPermissionLauncher;
    private CardView cardViewImg,cardViewAdmin;
    private ShapeableImageView imgProfile;
    private TextView tvEmail,tvName;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private String emailSession;
    private Button buttonLogout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        cardViewImg = view.findViewById(R.id.cardViewImg);
        cardViewAdmin = view.findViewById(R.id.cardViewAdministrator);
        imgProfile = view.findViewById(R.id.imgProfile);
        tvName = view.findViewById(R.id.nameUser);
        tvEmail = view.findViewById(R.id.emailUser);
        buttonLogout = view.findViewById(R.id.btnLogout);

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        openGallery();
                    } else {
                        Toast.makeText(getContext(), "Permission denied to read external storage", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data != null && data.getData() != null){
                            Uri imageUri = data.getData();
                            imgProfile.setImageURI(imageUri);
                            uploadImage(imageUri);
                        }
                    }
                }
        );

        cardViewImg.setOnClickListener(click -> {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        });

        emailSession = getLoggedInUserEmail();
        if (emailSession != null) {
            getSessionByUser(emailSession);
        } else {
            Log.e("ProfileFragment", "User email not found in SharedPreferences");
        }

        cardViewAdmin.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), Administrator.class);
            startActivity(intent);
        });

        buttonLogout.setOnClickListener(click -> {
            logout(emailSession);
        });

        return view;
    }


    private void navigateToLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityResultLauncher.launch(intent);
    }

    private void uploadImage(Uri uri) {
        try {
            // Convert URI to File
            File file = new File(FileUtils.getPath(getContext(), uri));

            // Create RequestBody instance from file
            RequestBody requestFile = RequestBody.create(MediaType.parse(getContext().getContentResolver().getType(uri)), file);

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

            // Add the email part
            String email = getLoggedInUserEmail();

            // Initialize API service
            ApiService apiService = ApiConfigAuthenticated.getApiService("muhammaddikiw02@gmail.com", "123456");

            // Create the call
            Call<User> call = apiService.addImage(email, body);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        Log.d("ProfileFragment", "Image upload successful");
                    } else {
                        Log.e("ProfileFragment", "Image upload failed: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("ProfileFragment", "Image upload error: " + t.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ProfileFragment", "File conversion error: " + e.getMessage());
        }
    }

    private void getSessionByUser(String email) {
        ApiService config = ApiConfigAuthenticated.getApiService("muhammaddikiw02@gmail.com", "123456");
        Call<SessionResponse> call = config.getSessionByUser(email);
        call.enqueue(new Callback<SessionResponse>() {
            @Override
            public void onResponse(Call<SessionResponse> call, Response<SessionResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<Session> sessionResponses = response.body().getData();
                    Session session = sessionResponses.get(0);
                    String roleName = session.getUser().getRole().getRoleName();
                    String email = session.getUser().getEmail();
                    String name = session.getUser().getName();
                    String image = session.getUser().getImage();
                    getRoleName(roleName);
                    displayUserDetails(name,email,image);
                    Log.d("ProfileFragment", "Successfully session user " + email + " and role " + roleName);
                }
            }

            @Override
            public void onFailure(Call<SessionResponse> call, Throwable t) {
                Log.e("ProfileFragment", "Session fetch error: " + t.getMessage());
            }
        });
    }

    public Bitmap decodeBase64ToBitmap(String base64Str) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void displayUserDetails(String name,String email,String image){
        tvName.setText(name);
        tvEmail.setText(email);
        if (image != null){
            Bitmap bitmap = decodeBase64ToBitmap(image);
            imgProfile.setImageBitmap(bitmap);
        }else {
            imgProfile.setImageBitmap(null);
        }
    }

    private String getLoggedInUserEmail() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("email", null);
    }

    private void getRoleName(String roleName){
        if (roleName.equals("Admin")){
            cardViewAdmin.setVisibility(View.VISIBLE);
        }
    }

    private void logout(String email){
        ApiService config = ApiConfigAuthenticated.getApiService(emailSession, "123456");
        Call<Void> call = config.logout(email);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    clearSharedPreferences();
                    navigateToLogin();
                    Log.d("ProfileFragment", "Logout successful");
                    Toast.makeText(requireActivity(), "Logout Successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(requireActivity(), "Logout Failed", Toast.LENGTH_LONG).show();
                    Log.e("ProfileFragment", "Logout failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ProfileFragment", "Logout error: " + t.getMessage());
            }
        });
    }

    private void clearSharedPreferences() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static class FileUtils {
        public static String getPath(Context context, Uri uri) {
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                String path = cursor.getString(columnIndex);
                cursor.close();
                return path;
            }
            return null;
        }
    }
}
