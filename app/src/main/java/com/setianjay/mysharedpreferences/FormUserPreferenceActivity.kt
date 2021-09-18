package com.setianjay.mysharedpreferences

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.setianjay.mysharedpreferences.databinding.ActivityFormUserPreferenceBinding

class FormUserPreferenceActivity : AppCompatActivity(), View.OnClickListener {
    private val binding by lazy { ActivityFormUserPreferenceBinding.inflate(layoutInflater) }

    private lateinit var model: UserModel

    private val userPreferences by lazy { UserPreferences(this) }

    companion object{
        const val EXTRA_TYPE_FORM = "extra_type_form"
        const val EXTRA_RESULT = "extra_result"
        const val RESULT_CODE = 101

        const val TYPE_ADD = 1
        const val TYPE_EDIT = 2

        private const val FIELD_IS_REQUIRED = "Field tidak boleh kosong"
        private const val FIELD_DIGIT_ONLY = "Hanya boleh terisi numerik"
        private const val FIELD_IS_NOT_VALID = "Email tidak valid"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initData()
        initListener()
    }

    /* function to initialize data */
    private fun initData(){
        // get data from intent
        model = intent.getParcelableExtra<UserModel>("USER") as UserModel
        val formType = intent.getIntExtra(EXTRA_TYPE_FORM, 0)

        var actionBarTitle = ""
        var btnTitle = ""

        when(formType){
            TYPE_ADD -> { // if form type equal with TYPE_ADD
                actionBarTitle = "Tambah Baru"
                btnTitle = "Simpan"
            }
            TYPE_EDIT -> { // if form type equal with TYPE_EDIT
                actionBarTitle = "Ubah"
                btnTitle = "Update"
                showPreferenceInForm()
            }
        }

        // set title and show the button back on action bar
        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // set btn text with btnTitle value
        binding.btnSave.text = btnTitle
    }

    /* function to show data user from preference */
    private fun showPreferenceInForm(){
        binding.edtName.setText(model.name)
        binding.edtEmail.setText(model.email)
        binding.edtAge.setText(model.age.toString())
        binding.edtPhone.setText(model.phoneNumber)
        if (model.isLove) {
            binding.rbYes.isChecked = true
        } else {
            binding.rbNo.isChecked = true
        }
    }

    /* function to initialize listener of view */
    private fun initListener(){
        binding.btnSave.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.btn_save){ // if view of id equal with R.id.btn_save
            val name = binding.edtName.text.toString().trim()
            val email = binding.edtEmail.text.toString().trim()
            val age = binding.edtAge.text.toString().trim()
            val phoneNumber = binding.edtPhone.text.toString().trim()
            val isLoveMu = binding.rgLoveMu.checkedRadioButtonId == R.id.rb_yes

            if(name.isEmpty()){ // if name is empty
                binding.edtName.error = FIELD_IS_REQUIRED
                return
            }

            if (email.isEmpty()){ // if email is empty
                binding.edtEmail.error = FIELD_IS_REQUIRED
                return
            }

            if (!isEmailValid(email)){ // if email invalid
                binding.edtEmail.error = FIELD_IS_NOT_VALID
                return
            }

            if (age.isEmpty()){ // if age is empty
                binding.edtAge.error = FIELD_IS_NOT_VALID
                return
            }

            if (phoneNumber.isEmpty()){ // if phoneNumber is empty
                binding.edtPhone.error = FIELD_IS_REQUIRED
                return
            }

            if (!TextUtils.isDigitsOnly(phoneNumber)){ // if phoneNumber not number
                binding.edtPhone.error = FIELD_DIGIT_ONLY
                return
            }

            // if all field passed from above condition, run this for save the value to preferences
            saveUser(name,email,age,phoneNumber,isLoveMu)

            // set and start intent to send the result to activity subscriber
            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_RESULT, model)
            setResult(RESULT_CODE, resultIntent)

            finish()
        }
    }

    /* function to save user data to preferences */
    private fun saveUser(name: String, email: String, age: String, phoneNumber: String, isLoveMu: Boolean){
        model.name = name
        model.email = email
        model.age = Integer.parseInt(age)
        model.phoneNumber = phoneNumber
        model.isLove = isLoveMu

        userPreferences.setUser(model)
        Toast.makeText(this, "Data tersimpan", Toast.LENGTH_SHORT).show()
    }

    /* function to check if email is valid or invalid */
    private fun isEmailValid(email: CharSequence): Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /* function to handle when option item in menu has selected */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) { // if item id equal with android.R.id.home
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}