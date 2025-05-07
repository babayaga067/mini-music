
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.minimusic.R
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {

    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize UI components
        val firstNameEditText = findViewById<EditText>(R.id.editTextFirstName)
        val lastNameEditText = findViewById<EditText>(R.id.editTextLastName)
        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
        val countrySpinner = findViewById<Spinner>(R.id.spinnerCountry)
        val dobEditText = findViewById<EditText>(R.id.editTextDOB)
        val genderRadioGroup = findViewById<RadioGroup>(R.id.radioGroupGender)
        val termsCheckBox = findViewById<CheckBox>(R.id.checkBoxTerms)
        val registerButton = findViewById<Button>(R.id.buttonRegister)
        val signInTextView = findViewById<TextView>(R.id.textViewSignIn)

        // Set up country spinner with a list of countries
        val countries = arrayOf("Select Country", "USA", "Canada", "UK", "Australia", "India")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countries)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        countrySpinner.adapter = adapter

        // Date Picker Dialog for selecting Date of Birth
        dobEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                dobEditText.setText("$selectedDay/${selectedMonth + 1}/$selectedYear") // Format selected date
            }, year, month, day)
            datePickerDialog.show()
        }

        // Register button click listener
        registerButton.setOnClickListener {
            val firstName = firstNameEditText.text.toString().trim()
            val lastName = lastNameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val country = countrySpinner.selectedItem.toString()
            val dob = dobEditText.text.toString().trim()
            val selectedGenderId = genderRadioGroup.checkedRadioButtonId
            val gender = if (selectedGenderId != -1) findViewById<RadioButton>(selectedGenderId).text.toString() else ""
            val termsAccepted = termsCheckBox.isChecked

            // Input validation to ensure all fields are filled correctly
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || country == "Select Country" || dob.isEmpty() || gender.isEmpty() || !termsAccepted) {
                Toast.makeText(this, "Please fill all fields and accept terms", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                // Navigate to another activity upon successful registration
            }
        }

        // Navigate to sign-in activity when the sign-in text is clicked
        signInTextView.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }
}