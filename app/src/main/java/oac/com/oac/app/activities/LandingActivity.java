package oac.com.oac.app.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.oacasia.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import oac.com.oac.app.adapter.ImagePagerAdapter;
import oac.com.oac.app.fragment.AwardNomineesFragment;
import oac.com.oac.app.fragment.HomeContainerFragment;
import oac.com.oac.app.fragment.MeFragment;
import oac.com.oac.app.fragment.PeopleToMeetFragment;
import oac.com.oac.app.fragment.QRCodeFragment;
import oac.com.oac.app.iHelper.IFragListener;
import oac.com.oac.app.load_toast.LoadToast;
import oac.com.oac.app.manager.SharedPrefManager;
import oac.com.oac.app.modal.BaseVO;
import oac.com.oac.app.modal.FeedParams;
import oac.com.oac.app.modal.ResponseError;
import oac.com.oac.app.networking.RequestManager;
import oac.com.oac.app.utils.APIMethods;
import oac.com.oac.app.utils.CircleTransform;
import oac.com.oac.app.utils.Util;

import static oac.com.oac.app.utils.Constants.BANNER_SLIDE_TIME;

/**
 * Created by Sudhir Singh on 04,July,2017
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class LandingActivity extends AppBaseActivity implements IFragListener {
    private final int TIME_INTERVAL = 2000;
    //    private SpaceTabLayout tabLayout;
//    private ViewPager viewPager;
    private long mBackPressed;

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;

    // Make sure to be using android.support.v7.app.ActionBarDrawerToggle version.
    // The android.support.v4.app.ActionBarDrawerToggle has been deprecated.
    private ActionBarDrawerToggle drawerToggle;

    private AutoScrollViewPager viewPager;

    private LoadToast loadToast;
    private ImageView img_OAC_Logo;

    private ArrayList<String> sponsorsList;
    private ImagePagerAdapter imagePagerAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.landing_activity);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.landing_toolbar);
        img_OAC_Logo = (ImageView) toolbar.findViewById(R.id.img_OAC_Logo);
        ImageView imgNotification = (ImageView) toolbar.findViewById(R.id.imgNotification);
//        toolbar.setNavigationIcon(R.drawable.ic_custom_drawer_icon);

        Picasso.with(LandingActivity.this).load(RequestManager.LIVE_SERVER +
                SharedPrefManager.getInstance().getSharedDataString(FeedParams.OAC_LOGO))
                .placeholder(R.drawable.app_logo).error(R.drawable.app_logo).into(img_OAC_Logo);

        String str = SharedPrefManager.getInstance().getSharedDataString(FeedParams.SPONSOR_BANNER);//you need to retrieve this string from shared preferences.
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        sponsorsList = new Gson().fromJson(str, type);

        viewPager = (AutoScrollViewPager) findViewById(R.id.sponsor_pager);

        imagePagerAdapter = new ImagePagerAdapter(LandingActivity.this, sponsorsList);
        viewPager.setAdapter(imagePagerAdapter.setInfiniteLoop(true));
        viewPager.setInterval(BANNER_SLIDE_TIME);
        viewPager.startAutoScroll();
        viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % sponsorsList.size());
//        viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % ListUtils.getSize(imageIdList));

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_custom_drawer_icon);

//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_custom_drawer_icon);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerToggle = setupDrawerToggle();
//        drawerToggle.setHomeAsUpIndicator(R.drawable.ic_custom_drawer_icon);
        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Inflate the header view at runtime
//        View headerLayout = nvDrawer.inflateHeaderView(R.layout.nav_header_testing);
// We can now look up items within the header if needed
        View header = nvDrawer.getHeaderView(0);
        final ImageView imgUserImg = (ImageView) header.findViewById(R.id.imgUserImg);
        final TextView txtName = (TextView) header.findViewById(R.id.txtName);
//        TextView txtEmail = (TextView) findViewById(R.id.txtEmail);

        setupDrawerContent(nvDrawer);

        if (savedInstanceState == null) {
            MenuItem item = nvDrawer.getMenu().getItem(0);
            selectDrawerItem(item);
        }

        imgNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notificationActivity = new Intent(LandingActivity.this, NotificationActivity.class);
                startActivity(notificationActivity);
            }
        });

        String user_image = SharedPrefManager.getInstance().getSharedDataString(FeedParams.IMAGE);
        String user_name = SharedPrefManager.getInstance().getSharedDataString(FeedParams.NAME);
        if (user_image != null && user_image.trim().length() > 0)
            Picasso.with(LandingActivity.this).load(user_image).placeholder(R.drawable.app_logo)
                    .error(R.drawable.app_logo).transform(new CircleTransform())
                    .resize(100, 100).into(imgUserImg);

        txtName.setText(user_name);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        loadToast = new LoadToast(LandingActivity.this);
        loadToast.setText("Please Wait...");
        loadToast.setTranslationY(height / 2);
        loadToast.setTextColor(Color.BLACK).setBackgroundColor(Color.WHITE)
                .setProgressColor(getResources().getColor(R.color.colorPrimary));


        mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                String user_image = SharedPrefManager.getInstance().getSharedDataString(FeedParams.IMAGE);
                if (user_image != null && user_image.trim().length() > 0) {
                    Picasso.with(LandingActivity.this).load(user_image).placeholder(R.drawable.app_logo)
                            .error(R.drawable.app_logo).transform(new CircleTransform())
                            .resize(100, 100).into(imgUserImg);
                }
                String user_name = SharedPrefManager.getInstance().getSharedDataString(FeedParams.NAME);
                txtName.setText(user_name);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null;
        switch (menuItem.getItemId()) {
            case R.id.home:
                fragmentClass = HomeContainerFragment.class;
                setNormalBanner();
                break;
            case R.id.edit_profile:
                fragmentClass = MeFragment.class;
                setNormalBanner();
                break;
//            case R.id.feedback:
//                fragmentClass = FeedBackFragment.class;
//                break;

            case R.id.people_to_meet:
                setNormalBanner();
                fragmentClass = PeopleToMeetFragment.class;
                break;


            case R.id.award_nominees:

                fragmentClass = AwardNomineesFragment.class;
                setJuryBanner();
                break;

            case R.id.qr_code:
                setNormalBanner();
                fragmentClass = QRCodeFragment.class;
                break;

            case R.id.log_out:
                if (Util.isNetworkOnline(LandingActivity.this)) {
                    HashMap<String, String> params = new HashMap<>();
                    String userId = SharedPrefManager.getInstance().getSharedDataString(FeedParams.USER_ID);
                    String token = SharedPrefManager.getInstance().getSharedDataString(FeedParams.TOKEN);
                    params.put(FeedParams.USER_ID, userId);
                    params.put(FeedParams.TOKEN, token);

                    placeRequest(APIMethods.SIGN_OUT, BaseVO.class, params, true, null);
                } else {
                    showToast(getResources().getString(R.string.no_internet));
                }
                break;


            default:
                fragmentClass = HomeContainerFragment.class;
        }

        if (fragmentClass != null) {
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

            // Highlight the selected item has been done by NavigationView
            menuItem.setChecked(true);
            // Set action bar title
            setTitle(menuItem.getTitle());
            // Close the navigation drawer
            mDrawer.closeDrawers();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public Fragment getFragmentFromViewpager(ViewPager mViewPager) {
        return ((Fragment) (mViewPager.getAdapter().instantiateItem(mViewPager, mViewPager.getCurrentItem())));
    }

    @Override
    public void onAPIResponse(Object response, String apiMethod) {
        super.onAPIResponse(response, apiMethod);
        loadToast.success();
        if (apiMethod.equalsIgnoreCase(APIMethods.SIGN_OUT)) {
            BaseVO baseVO = (BaseVO) response;

            showToast(baseVO.getMessage());

            SharedPrefManager.getInstance().clearPreference();
            Intent loginIntent = new Intent(LandingActivity.this, SignInActivity.class);
            startActivity(loginIntent);
            LandingActivity.this.finish();

        }
    }

    @Override
    public void onErrorResponse(VolleyError error, String apiMethod) {
        super.onErrorResponse(error, apiMethod);
        loadToast.error();

        if (apiMethod.equalsIgnoreCase(APIMethods.SIGN_OUT)) {

            ResponseError responseError = (ResponseError) error;
            if (responseError.getErrorMessage().equalsIgnoreCase(getResources().getString(R.string.invalid_token))) {
                SharedPrefManager.getInstance().clearPreference();
                Intent loginIntent = new Intent(LandingActivity.this, SignInActivity.class);
                startActivity(loginIntent);
                LandingActivity.this.finish();

            }
            showToast(responseError.getErrorMessage());
        }
    }

    @Override
    public void changeFragment(String tag) {

        if (tag.equalsIgnoreCase(getResources().getString(R.string.jury_tag))) {

            setJuryBanner();


        } else {

            setNormalBanner();


        }
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE 1: Make sure to override the method with only a single `Bundle` argument
    // Note 2: Make sure you implement the correct `onPostCreate(Bundle savedInstanceState)` method.
    // There are 2 signatures and only `onPostCreate(Bundle state)` shows the hamburger icon.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    private void setNormalBanner() {
        sponsorsList.clear();
        String str = SharedPrefManager.getInstance().getSharedDataString(FeedParams.SPONSOR_BANNER);//you need to retrieve this string from shared preferences.
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        sponsorsList = new Gson().fromJson(str, type);

        viewPager.setAdapter(new ImagePagerAdapter(LandingActivity.this, sponsorsList).setInfiniteLoop(true));
//            viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % sponsorsList.size());
        imagePagerAdapter.notifyDataSetChanged();
        viewPager.startAutoScroll();

        Picasso.with(LandingActivity.this).load(RequestManager.LIVE_SERVER
                + SharedPrefManager.getInstance().getSharedDataString(FeedParams.OAC_LOGO))
                .into(img_OAC_Logo);
    }

    private void setJuryBanner() {
        sponsorsList.clear();
        sponsorsList.add("images/oaa-banner.jpg");

        viewPager.setAdapter(new ImagePagerAdapter(LandingActivity.this, sponsorsList).setInfiniteLoop(true));
//            viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % sponsorsList.size());
        imagePagerAdapter.notifyDataSetChanged();
        viewPager.stopAutoScroll();

        Picasso.with(LandingActivity.this).load(RequestManager.LIVE_SERVER
                + SharedPrefManager.getInstance().getSharedDataString(FeedParams.OAA_LOGO))
                .into(img_OAC_Logo);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }


//    //we need the outState to save the position
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        tabLayout.saveState(outState);
//        super.onSaveInstanceState(outState);
//    }

    @Override
    public void onBackPressed() {

//        Fragment fragment = getFragmentFromViewpager(viewPager);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.flContent);
        if (fragment instanceof HomeContainerFragment && ((HomeContainerFragment) fragment).isAvailableFragments()) {
            setNormalBanner();
            ((HomeContainerFragment) fragment).initialzeView();
        } else if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Press again to exit",
                    Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }


    @Override
    protected void onPause() {
        super.onPause();
        // stop auto scroll when onPause
        viewPager.stopAutoScroll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // start auto scroll when onResume
        viewPager.startAutoScroll();
    }
}
