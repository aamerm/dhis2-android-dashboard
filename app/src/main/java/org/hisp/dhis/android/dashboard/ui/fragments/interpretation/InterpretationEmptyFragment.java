package org.hisp.dhis.android.dashboard.ui.fragments.interpretation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import org.hisp.dhis.android.dashboard.DhisService;
import org.hisp.dhis.android.dashboard.R;
import org.hisp.dhis.android.dashboard.api.job.NetworkJob;
import org.hisp.dhis.android.dashboard.api.network.SessionManager;
import org.hisp.dhis.android.dashboard.api.persistence.preferences.ResourceType;
import org.hisp.dhis.android.dashboard.api.utils.NetworkUtils;
import org.hisp.dhis.android.dashboard.ui.fragments.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by arazabishov on 7/24/15.
 */
public class InterpretationEmptyFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = InterpretationEmptyFragment.class.getSimpleName();
    private static final String IS_LOADING = "state:isLoading";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.progress_bar)
    SmoothProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_interpretations_empty, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        mToolbar.setNavigationIcon(R.mipmap.ic_menu);
        mToolbar.setNavigationOnClickListener(this);
        mToolbar.setTitle(R.string.interpretations);
        mToolbar.inflateMenu(R.menu.menu_interpretations_fragment);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onMenuItemClicked(item);
            }
        });

        if (isDhisServiceBound() &&
                !getDhisService().isJobRunning(DhisService.SYNC_INTERPRETATIONS) &&
                !SessionManager.getInstance().isResourceTypeSynced(ResourceType.INTERPRETATIONS)) {
            syncInterpretations();
        }


        boolean isLoading = isDhisServiceBound() &&
                getDhisService().isJobRunning(DhisService.SYNC_INTERPRETATIONS);
        if ((savedInstanceState != null && savedInstanceState.getBoolean(IS_LOADING))  || isLoading) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
        if(!NetworkUtils.checkConnection(getActivity())) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        toggleNavigationDrawer();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(IS_LOADING, mProgressBar
                .getVisibility() == View.VISIBLE);
        super.onSaveInstanceState(outState);
    }

    public boolean onMenuItemClicked(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh: {
                if(!NetworkUtils.checkConnection(getActivity())){
                    Toast.makeText(
                            getActivity(), R.string.no_network_connection, LENGTH_SHORT).show();
                }
                else
                    syncInterpretations();
                return true;
            }
        }
        return false;
    }

    private void syncInterpretations() {
        if (isDhisServiceBound()) {
            getDhisService().syncInterpretations();
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onResponseReceived(NetworkJob.NetworkJobResult<?> result) {
        if (result.getResourceType() == ResourceType.INTERPRETATIONS) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}
