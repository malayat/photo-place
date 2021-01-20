package ec.solmedia.photoplace.login.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ec.solmedia.photoplace.PhotoPlaceApp;
import ec.solmedia.photoplace.R;
import ec.solmedia.photoplace.main.ui.MainActivity;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.btnLogin)
    LoginButton btnLogin;
    @BindView(R.id.container)
    RelativeLayout container;

    @Inject
    SharedPreferences sharedPreferences;

    private static final String TAG = "LoginActivity";

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        setupInjection();

        if (AccessToken.getCurrentAccessToken() != null) {
            navigateToMainScreen();
            return;
        }

        callbackManager = CallbackManager.Factory.create();
        //btnLogin.setPublishPermissions(Arrays.asList("publish_actions"));
        btnLogin.setReadPermissions(Arrays.asList("public_profile", "user_friends", "email"));
        btnLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    String name = object.getString("name");
                                    Log.e(TAG, "NAME======> " + name);
                                    sharedPreferences.edit().putString(PhotoPlaceApp.NAME_KEY, name).commit();
                                    String email = object.getString("email");
                                    Log.e(TAG, "EMAIL ======> " + email);
                                    sharedPreferences.edit().putString(PhotoPlaceApp.EMAIL_KEY, email).commit();
                                    String id = object.getString("id");
                                    sharedPreferences.edit().putString(PhotoPlaceApp.ID_KEY, id).commit();
                                    Log.e(TAG, " ID ======> " + id);

                                    navigateToMainScreen();

                                } catch (JSONException e) {
                                    Log.e(TAG, "ERROR!!!!!! " + e.getLocalizedMessage());
                                    e.printStackTrace();
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Snackbar.make(container, R.string.login_cancel_error, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                String msgError = String.format(getString(R.string.login_error), error.getMessage());
                Snackbar.make(container, msgError, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void setupInjection() {
        PhotoPlaceApp app = (PhotoPlaceApp) getApplication();
        app.getLoginComponent().inject(this);
    }

    private void navigateToMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
