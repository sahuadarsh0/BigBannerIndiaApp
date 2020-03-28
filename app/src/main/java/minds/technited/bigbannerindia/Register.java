package minds.technited.bigbannerindia;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import minds.technited.asautils.MD;
import minds.technited.bigbannerindia.models.Customer;
import minds.technited.bigbannerindia.models.Masters;
import minds.technited.bigbannerindia.models.Received;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Register extends Fragment {


    private Context context, c, ca;
    private String email, mobile1, state_id, district_id, city_id, locality_id, postal_code_id, password, cnf_password, name, address, gender;
    private TextInputEditText etName, etMobile1, etEmail, etAddress, etPassword, etcnf_password;
    private List<String> state_ids, district_ids, city_ids, locality_ids, postal_code_ids;
    private Spinner state_spinner, district_spinner, city_spinner, locality_spinner, postal_code_spinner;
    private ChipGroup choiceChipGroup;

    public Register() {
        // Required empty public constructor
    }


    Register(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register,
                container, false);

        c = getActivity().getApplicationContext();
        state_ids = district_ids = city_ids = locality_ids = postal_code_ids = new ArrayList<>();
        state_id = district_id = city_id = locality_id = postal_code_id = new String();
        ConstraintLayout layout = view.findViewById(R.id.request_layout);
        Button btnRegister = view.findViewById(R.id.btnRegister);

        etName = view.findViewById(R.id.name);
        etMobile1 = view.findViewById(R.id.mobile1);
        etEmail = view.findViewById(R.id.email);
        etAddress = view.findViewById(R.id.address);
        etPassword = view.findViewById(R.id.password);
        etcnf_password = view.findViewById(R.id.cnf_password);

        Animation animation = AnimationUtils.loadAnimation(c, R.anim.slide_in_up);
        layout.startAnimation(animation);
        state_spinner = view.findViewById(R.id.state);
        district_spinner = view.findViewById(R.id.district);
        city_spinner = view.findViewById(R.id.city);
        locality_spinner = view.findViewById(R.id.locality);
        postal_code_spinner = view.findViewById(R.id.postalcode);

        TextView login_text = view.findViewById(R.id.login_text);
        TextView register_text = view.findViewById(R.id.register_text);


        login_text.setOnClickListener(v -> getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, new Login(context))
                .commit()
        );
        register_text.setOnClickListener(v -> {

        });

        gender = "";
        choiceChipGroup = view.findViewById(R.id.gender);
        choiceChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            gender = "";
            if (checkedId % 2 == 1) {
                gender = "Male";
            } else if (checkedId % 2 == 0) {
                gender = "Female";
            }

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

        btnRegister.setOnClickListener(v -> customerRegistration());


        return view;
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

    // TODO: 27-Mar-20 Need to send correct gender 
    private void customerRegistration() {

        name = etName.getText().toString();
        mobile1 = etMobile1.getText().toString();
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();
        cnf_password = etcnf_password.getText().toString();
        address = etAddress.getText().toString();

        boolean flag = false;

        if (name.isEmpty()) {
            Toast.makeText(c, "Name is empty", Toast.LENGTH_SHORT).show();
        } else if (mobile1.isEmpty()) {
            Toast.makeText(c, "Mobile number is empty", Toast.LENGTH_SHORT).show();
        }else if (gender.equals("")) {
            Toast.makeText(c, "Select Gender", Toast.LENGTH_SHORT).show();
        } else if (!mobile1.matches("[0-9]{10}")) {
            Toast.makeText(c, "Mobile number is invalid", Toast.LENGTH_SHORT).show();
        } else if (email.isEmpty()) {
            Toast.makeText(c, "Email is empty", Toast.LENGTH_SHORT).show();
        } else if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            Toast.makeText(c, "Invalid email id", Toast.LENGTH_SHORT).show();
        } else if (state_id.isEmpty()) {
            Toast.makeText(c, "Select state", Toast.LENGTH_SHORT).show();
        } else if (district_id.isEmpty()) {
            Toast.makeText(c, "Select district", Toast.LENGTH_SHORT).show();
        } else if (city_id.isEmpty()) {
            Toast.makeText(c, "Select City", Toast.LENGTH_SHORT).show();
        } else if (address.isEmpty()) {
            Toast.makeText(c, "Address is empty", Toast.LENGTH_SHORT).show();
        } else if (postal_code_id.isEmpty()) {
            Toast.makeText(c, "Postal Code is empty", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(c, "Password is empty", Toast.LENGTH_SHORT).show();
        } else if (cnf_password.isEmpty()) {
            Toast.makeText(c, "Confirm password is missing", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(cnf_password)) {
            Toast.makeText(c, "Password did not matched", Toast.LENGTH_SHORT).show();
        } else {
            flag = true;
        }

        if (flag) {

            Customer customer = new Customer("", name, gender, state_id, district_id, city_id, locality_id, postal_code_id, mobile1, address, email, password);

            Call<Received> createCustomerCall = AccountsApi.getApiService().registerCustomer(customer);
            createCustomerCall.enqueue(new Callback<Received>() {
                @Override
                public void onResponse(@NotNull Call<Received> call, @NotNull Response<Received> response) {

                    Received data = response.body();
                    Fragment loginFrag = new Login(getActivity());

                    if (data.getMsg().equals("Successful"))
                        MD.alert(getActivity(), data.getMsg(), data.getDetails(), "Login", R.id.main_container, loginFrag);

                    else
                        MD.alert(context, data.getMsg(), data.getDetails());

                }

                @Override
                public void onFailure(@NotNull Call<Received> call, @NotNull Throwable t) {
                    Log.e("ASA", "onFailure: " + t.getMessage(), t);
                }
            });

        }

    }
}


