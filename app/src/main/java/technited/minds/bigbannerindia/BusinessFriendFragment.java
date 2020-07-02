package technited.minds.bigbannerindia;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import technited.minds.androidutils.MD;
import technited.minds.bigbannerindia.models.BusinessFriendModel;
import technited.minds.bigbannerindia.models.Masters;
import technited.minds.bigbannerindia.models.Received;


public class BusinessFriendFragment extends Fragment {


    private Context context;
    private String email, mobile1, state_id, district_id, city_id, locality_id, postal_code_id, name, address, gender, father_name, dob, nationality, qualification, marital_status;
    private TextInputEditText etName, etFatherName, etDob, etNationality, etQualification, etMaritalStatus, etMobile1, etEmail, etAddress;
    private List<String> state_ids, district_ids, city_ids, locality_ids, postal_code_ids;
    private Spinner state_spinner, district_spinner, city_spinner, locality_spinner, postal_code_spinner;
    private ChipGroup choiceChipGroup;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_business_friend, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = requireContext();
        state_ids = district_ids = city_ids = locality_ids = postal_code_ids = new ArrayList<>();
        state_id = district_id = city_id = locality_id = postal_code_id = new String();
        ConstraintLayout layout = view.findViewById(R.id.request_layout);
        Button btnRegister = view.findViewById(R.id.btnRegister);

        etName = view.findViewById(R.id.name);
        etMobile1 = view.findViewById(R.id.mobile1);
        etEmail = view.findViewById(R.id.email);
        etAddress = view.findViewById(R.id.address);
        etFatherName = view.findViewById(R.id.father_name);
        etDob = view.findViewById(R.id.dob);
        etNationality = view.findViewById(R.id.nationality);
        etQualification = view.findViewById(R.id.qualification);
        etMaritalStatus = view.findViewById(R.id.marital_status);

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_up);
        layout.startAnimation(animation);
        state_spinner = view.findViewById(R.id.state);
        district_spinner = view.findViewById(R.id.district);
        city_spinner = view.findViewById(R.id.city);
        locality_spinner = view.findViewById(R.id.locality);
        postal_code_spinner = view.findViewById(R.id.postalcode);


        gender = "";
        choiceChipGroup = view.findViewById(R.id.gender);
        choiceChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            gender = "";
            if (checkedId % 2 == 0) {
                gender = "Male";
            } else if (checkedId % 2 == 1) {
                gender = "Female";
            }

            Log.d("asa", "checkedId: " + checkedId);

        });

        // Get Spinners
        {
            getStateList();

            state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    state_id = state_ids.get(position);
                    getDistrictList(state_id);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            district_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    district_id = district_ids.get(position);
                    getCityList(district_id);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    city_id = city_ids.get(position);
                    getLocalityList(city_id);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            locality_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    locality_id = locality_ids.get(position);
                    getPostalCodeList(locality_id);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            postal_code_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    postal_code_id = postal_code_ids.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        btnRegister.setOnClickListener(v -> businessFriendRegistration());


    }

    private void getStateList() {


        Call<List<Masters>> getState = AccountsApi.getApiService().getStates();

        getState.enqueue(new Callback<List<Masters>>() {
            @Override
            public void onResponse(@NotNull Call<List<Masters>> call, @NotNull Response<List<Masters>> response) {
                List<Masters> states = response.body();
                state_ids = loadMasters(states, state_ids, state_spinner);

            }

            @Override
            public void onFailure(@NotNull Call<List<Masters>> call, @NotNull Throwable t) {
            }
        });


    }

    private void getDistrictList(String state_id) {

        Call<List<Masters>> getDistrict = AccountsApi.getApiService().getDistricts(state_id);
        getDistrict.enqueue(new Callback<List<Masters>>() {
            @Override
            public void onResponse(@NotNull Call<List<Masters>> call, @NotNull Response<List<Masters>> response) {

                List<Masters> districts = response.body();
                district_ids = loadMasters(districts, district_ids, district_spinner);

            }

            @Override
            public void onFailure(@NotNull Call<List<Masters>> call, @NotNull Throwable t) {

            }
        });

    }

    private void getCityList(String district_id) {
        Call<List<Masters>> getCity = AccountsApi.getApiService().getCities(district_id);
        getCity.enqueue(new Callback<List<Masters>>() {
            @Override
            public void onResponse(@NotNull Call<List<Masters>> call, @NotNull Response<List<Masters>> response) {

                List<Masters> cities = response.body();
                city_ids = loadMasters(cities, city_ids, city_spinner);

            }

            @Override
            public void onFailure(@NotNull Call<List<Masters>> call, @NotNull Throwable t) {

            }
        });

    }

    private void getLocalityList(String city_id) {
        Call<List<Masters>> getLocality = AccountsApi.getApiService().getLocalities(city_id);
        getLocality.enqueue(new Callback<List<Masters>>() {
            @Override
            public void onResponse(@NotNull Call<List<Masters>> call, @NotNull Response<List<Masters>> response) {
                List<Masters> localities = response.body();
                locality_ids = loadMasters(localities, locality_ids, locality_spinner);
            }

            @Override
            public void onFailure(@NotNull Call<List<Masters>> call, @NotNull Throwable t) {

            }
        });

    }

    private void getPostalCodeList(String locality_id) {
        Call<List<Masters>> getPostalCode = AccountsApi.getApiService().getPostalCodes(locality_id);
        getPostalCode.enqueue(new Callback<List<Masters>>() {
            @Override
            public void onResponse(@NotNull Call<List<Masters>> call, @NotNull Response<List<Masters>> response) {

                List<Masters> codes = response.body();
                postal_code_spinner.setAdapter(null);
                List<String> newIds = new ArrayList<>();
                newIds.clear();
                if (codes != null) {
                    String[] s = new String[codes.size()];
                    for (int i = 0; i < codes.size(); i++) {
                        s[i] = codes.get(i).getCode();
                        final ArrayAdapter<String> a = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, s);
                        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        newIds.add(codes.get(i).getId());
                        postal_code_spinner.setAdapter(a);
                    }

                    postal_code_ids = newIds;
                }


            }

            @Override
            public void onFailure(@NotNull Call<List<Masters>> call, @NotNull Throwable t) {

                Log.e("ASA", "onFailure: " + t.getMessage(), t);
            }
        });

    }

    private List<String> loadMasters(List<Masters> master, List<String> ids, @NotNull Spinner spinner) {
        spinner.setAdapter(null);
        List<String> newIds = new ArrayList<>();
        newIds.clear();
        if (master != null) {
            String[] s = new String[master.size()];
            for (int i = 0; i < master.size(); i++) {
                s[i] = master.get(i).getName();
                final ArrayAdapter<String> a = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, s);
                a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                newIds.add(master.get(i).getId());
                spinner.setAdapter(a);
            }

            ids = newIds;
        }
        return ids;
    }

    private void businessFriendRegistration() {

        name = etName.getText().toString();
        mobile1 = etMobile1.getText().toString();
        email = etEmail.getText().toString();

        father_name = etFatherName.getText().toString();
        dob = etDob.getText().toString();
        nationality = etNationality.getText().toString();
        qualification = etQualification.getText().toString();
        marital_status = etMaritalStatus.getText().toString();

        address = etAddress.getText().toString();

        boolean flag = false;

        if (name.isEmpty()) {
            Toast.makeText(context, "Name is empty", Toast.LENGTH_SHORT).show();
        } else if (mobile1.isEmpty()) {
            Toast.makeText(context, "Mobile number is empty", Toast.LENGTH_SHORT).show();
        } else if (father_name.isEmpty()) {
            Toast.makeText(context, "Father's Name is empty", Toast.LENGTH_SHORT).show();
        } else if (dob.isEmpty()) {
            Toast.makeText(context, "DOB is empty", Toast.LENGTH_SHORT).show();
        } else if (nationality.isEmpty()) {
            Toast.makeText(context, "Nationality is empty", Toast.LENGTH_SHORT).show();
        } else if (qualification.isEmpty()) {
            Toast.makeText(context, "Qualification is empty", Toast.LENGTH_SHORT).show();
        } else if (marital_status.isEmpty()) {
            Toast.makeText(context, "Marital Status is empty", Toast.LENGTH_SHORT).show();
        } else if (gender.equals("")) {
            Toast.makeText(context, "Select Gender", Toast.LENGTH_SHORT).show();
        } else if (!mobile1.matches("[0-9]{10}")) {
            Toast.makeText(context, "Mobile number is invalid", Toast.LENGTH_SHORT).show();
        } else if (email.isEmpty()) {
            Toast.makeText(context, "Email is empty", Toast.LENGTH_SHORT).show();
        } else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            Toast.makeText(context, "Invalid email id", Toast.LENGTH_SHORT).show();
        } else if (state_id.isEmpty()) {
            Toast.makeText(context, "Select state", Toast.LENGTH_SHORT).show();
        } else if (district_id.isEmpty()) {
            Toast.makeText(context, "Select district", Toast.LENGTH_SHORT).show();
        } else if (city_id.isEmpty()) {
            Toast.makeText(context, "Select City", Toast.LENGTH_SHORT).show();
        } else if (address.isEmpty()) {
            Toast.makeText(context, "Address is empty", Toast.LENGTH_SHORT).show();
        } else if (postal_code_id.isEmpty()) {
            Toast.makeText(context, "Postal Code is empty", Toast.LENGTH_SHORT).show();
        } else {
            flag = true;
        }

        if (flag) {

            BusinessFriendModel friend = new BusinessFriendModel("", name, gender, state_id, district_id, city_id, locality_id, postal_code_id, mobile1, address, email, father_name, dob, nationality, qualification, marital_status);

            Call<Received> createFriendCall = AccountsApi.getApiService().registerBusinessFriend(friend);
            createFriendCall.enqueue(new Callback<Received>() {
                @Override
                public void onResponse(@NotNull Call<Received> call, @NotNull Response<Received> response) {

                    Received data = response.body();

                    NavDirections action = BusinessFriendFragmentDirections.actionBusinessFriendFragmentToHomeFragment();
                    MD.alert(getActivity(), data.getMsg(), data.getDetails(), "ok", getView(), action);

                }

                @Override
                public void onFailure(@NotNull Call<Received> call, @NotNull Throwable t) {
                    Log.e("ASA", "onFailure: " + t.getMessage(), t);
                }

            });

        }

    }
}


