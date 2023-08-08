package com.github.krxwl.codecat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout

private const val TAG = "AuthenticationFragment"

class AuthenticationFragment : Fragment(R.layout.fragment_authentication) {

    private lateinit var codeCatTextView: TextView
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var forgotPasswordButton: Button

    private lateinit var passwordTextInputLayout: TextInputLayout
    private lateinit var emailTextInputLayout: TextInputLayout


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_authentication, container, false)

        codeCatTextView = view.findViewById(R.id.logo_codecat) as TextView
        loginButton = view.findViewById(R.id.login_button) as Button
        registerButton = view.findViewById(R.id.register_button) as Button
        forgotPasswordButton = view.findViewById(R.id.forgot_password_button) as Button
        passwordTextInputLayout = view.findViewById(R.id.password_text_input) as TextInputLayout
        emailTextInputLayout = view.findViewById(R.id.email_text_input) as TextInputLayout



        return view

    }

    companion object {
        fun newInstance(): AuthenticationFragment {
            return AuthenticationFragment()
        }
    }
}