package io.github.kschaap1994.roosterapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.kschaap1994.roosterapp.R;
import io.github.kschaap1994.roosterapp.activity.MainActivity;
import io.github.kschaap1994.roosterapp.database.DbLab;

/**
 * Created by Kevin on 20-10-2016.
 */

public class SettingsFragment extends Fragment {

    @BindView(R.id.add_class)
    public EditText addClass;
    @BindView(R.id.add_first_name)
    public EditText addFirstName;
    @BindView(R.id.add_last_name)
    public EditText addLastName;

    private final String FRAGMENT_TITLE = "Options";

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);

        setSettings();

        getActivity().setTitle(FRAGMENT_TITLE);

        return view;
    }

    public void setSettings() {
        final DbLab lab = DbLab.get(getActivity());

        addClass.setText(lab.getSetting("class"));
        addFirstName.setText(lab.getSetting("firstName"));
        addLastName.setText(lab.getSetting("lastName"));
    }

    @OnClick(R.id.save_settings_button)
    public void saveSettings() {
        if (!validate()) return;

        final String className = addClass.getText().toString();
        final String firstName = addFirstName.getText().toString();
        final String lastName = addLastName.getText().toString();

        final DbLab lab = DbLab.get(getActivity());

        lab.addOrUpdateSetting("class", className);
        lab.addOrUpdateSetting("firstName", firstName);
        lab.addOrUpdateSetting("lastName", lastName);

        final MainActivity activity = (MainActivity) getActivity();
        final Fragment fragment = getFragmentManager().findFragmentByTag("settings");

        if (fragment != null) {
            getFragmentManager().popBackStack("settings",
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        activity.setupFragment();
    }

    private boolean validate() {
        boolean valid = true;
        if (addClass.getText().toString().isEmpty()) {
            addClass.setError("Classname is empty");
            valid = false;
        } else
            addClass.setError(null);

        if (addFirstName.getText().toString().isEmpty()) {
            addFirstName.setError("First name is empty");
            valid = false;
        } else
            addFirstName.setError(null);

        if (addLastName.getText().toString().isEmpty()) {
            addLastName.setError("Last name is empty");
            valid = false;
        } else
            addLastName.setError(null);

        return valid;
    }
}
