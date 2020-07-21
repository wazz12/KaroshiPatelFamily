package com.karoshi.patelfamily.feature.list

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.karoshi.patelfamily.R
import com.karoshi.patelfamily.database.Family
import com.karoshi.patelfamily.feature.ItemClickListener
import com.karoshi.patelfamily.utils.inflate
import com.karoshi.patelfamily.utils.visibleIfTrue
import com.karoshi.patelfamily.viewModel.FamilyViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.family_list_item.view.*

class FamilyListAdapter(
    private val familyList: List<Family>,
    private val viewModel: FamilyViewModel
) :
    RecyclerView.Adapter<FamilyListAdapter.ViewHolder>() {

    private lateinit var itemClickListener: ItemClickListener
    private val disposable = CompositeDisposable()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.family_list_item))

    override fun getItemCount() = familyList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(familyList[position])
        holder.bindClickListener(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(family: Family) {
            with(itemView) {
                user_name_text_view.text = context.getString(
                    R.string.user_full_name,
                    family.userName,
                    family.userSurname
                )

                father_name_text_view.visibleIfTrue(false)
                if (family.fatherId.isNotBlank()) {
                    disposable.add(
                        viewModel.getFatherName(family.fatherId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                father_name_text_view.visibleIfTrue(it.isNotBlank())
                                if (it.isNotBlank()) {
                                    father_name_text_view.text = context.getString(
                                        R.string.father_name, it, family.userSurname
                                    )
                                }
                            }, { error ->
                                Log.e("getFatherName", error.toString())
                            })
                    )
                }
            }
        }

        fun bindClickListener(position: Int) {
            itemView.setOnClickListener {
                itemClickListener.onItemClick(position)
            }
        }
    }

    fun getItem(position: Int): Family {
        return familyList[position]
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}