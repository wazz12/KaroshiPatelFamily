package com.karoshi.patelfamily.feature.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.karoshi.patelfamily.R
import com.karoshi.patelfamily.database.Family
import com.karoshi.patelfamily.feature.BaseFragment
import com.karoshi.patelfamily.feature.ItemClickListener
import com.karoshi.patelfamily.feature.appToolbarTitle
import com.karoshi.patelfamily.utils.visibleIfTrue
import com.karoshi.patelfamily.viewModel.FamilyViewModel
import com.karoshi.patelfamily.viewModel.Injection
import com.karoshi.patelfamily.viewModel.ViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_family_list.*

class FamilyListFragment : BaseFragment(), ItemClickListener {

    private lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: FamilyViewModel by viewModels { viewModelFactory }
    private val disposable = CompositeDisposable()

    private lateinit var familyListAdapter: FamilyListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_family_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelFactory = Injection.provideViewModelFactory(requireContext())
        setToolBar(getString(R.string.app_name))
        setSearchView()
        getFamilyList("")
    }

    override fun onStop() {
        super.onStop()
        hideProgressSpinnerDialog()
    }

    private fun setToolBar(title: String) {
        toolbarNavIcon.visibleIfTrue(false)
        toolbar.appToolbarTitle(title)
    }

    private fun setSearchView() {
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                Log.e("onQueryTextChange", "query:$query")
                if (!query.isNullOrBlank())
                    getFamilyList(query)
                else
                    getFamilyList("")
                return false
            }
        })
    }

    private fun getFamilyList(searchString: String) {
        disposable.add(
            viewModel.getFamilyList(searchString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ this.setFamilyList(it) },
                    { error ->
                        Log.e("getFamilyList", error.toString())
                    })
        )
    }

    private fun setFamilyList(familyList: List<Family>) {
//        Log.e("setFamilyList:", familyList.toString())
        familyListAdapter =
            FamilyListAdapter(
                familyList,
                viewModel
            )
        recycler_view.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler_view.adapter = familyListAdapter
        familyListAdapter.setItemClickListener(this)
        Log.e("setFamilyList", "itemCount:" + familyListAdapter.itemCount)
    }

    override fun onItemClick(position: Int) {
        val userDetails = familyListAdapter.getItem(position)
//        openUserDetail(userDetails)
    }
}