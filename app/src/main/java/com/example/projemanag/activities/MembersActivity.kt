package com.example.projemanag.activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projemanag.R
import com.example.projemanag.adapters.MemberListItemsAdapter
import com.example.projemanag.databinding.ActivityMembersBinding
import com.example.projemanag.firebase.FirestoreClass
import com.example.projemanag.models.Board
import com.example.projemanag.models.User
import com.example.projemanag.utils.Constants

class MembersActivity : BaseActivity() {
    private lateinit var binding: ActivityMembersBinding
    private lateinit var mBoardDetails: Board

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMembersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.BOARD_DETAIL)) {
            mBoardDetails = intent.getParcelableExtra(Constants.BOARD_DETAIL)!!
        }

        setupActionBar(binding.toolbarMembersActivity, resources.getString(R.string.members))

        showProgressDialog()
        FirestoreClass().getAssignedMembersListDetails(this, mBoardDetails.assignedTo)
    }

    fun setupMembersList(list: ArrayList<User>) {
        hideProgressDialog()

        binding.rvMembersList.let {
            it.layoutManager = LinearLayoutManager(this)
            it.setHasFixedSize(true)
            val adapter = MemberListItemsAdapter(this, list)
            it.adapter = adapter
        }

    }
}