package com.yewapp.ui.modules.refer.adapter

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.yewapp.data.network.api.refer.PhoneContacts
import com.yewapp.databinding.ItemChooseContactBinding
import com.yewapp.ui.base.BaseRecyclerAdapter
import com.yewapp.ui.modules.refer.vm.ItemChooseContactsViewModel


class ChooseContactsAdapter(
    var contactDataList: ArrayList<PhoneContacts>,
    var selectedItems: SparseBooleanArray,
    val listener: ItemChooseContactsViewModel.OnItemClickListener
) : BaseRecyclerAdapter<ChooseContactsAdapter.ChooseContactsViewHolder, PhoneContacts>(
    contactDataList
), Filterable {

    var contactList = mutableListOf<PhoneContacts>()
    var contactListFiltered = mutableListOf<PhoneContacts>()
    var selectedContact = SparseBooleanArray()
    var selectedContactFiltered = SparseBooleanArray()

    init {
        contactList = contactDataList
        selectedContact = selectedItems
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ChooseContactsViewHolder =
        ChooseContactsViewHolder(
            ItemChooseContactBinding
                .inflate(
                    LayoutInflater
                        .from(parent.context), parent, false
                )
        )

    override fun onBindViewHolder(holder: ChooseContactsViewHolder, position: Int) {
        holder.bind(ItemChooseContactsViewModel(contactListFiltered[position], listener))
//        holder.bind((ItemChooseContactsViewModel(listData[position])))
    }

    override fun getItemCount(): Int {
        return contactListFiltered.size
    }

    class ChooseContactsViewHolder(
        private val binding: ItemChooseContactBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: ItemChooseContactsViewModel) {
            binding.viewModel = viewModel
            binding.contactName.text = viewModel.phoneContacts.name
            binding.PhoneNumber.text = viewModel.phoneContacts.phone
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

//    fun addItems(list: List<PhoneContacts>) {
//        listData.addAll(list)
//        notifyDataSetChanged()
//    }

    fun addItems(list: List<PhoneContacts>, selectedItems: SparseBooleanArray) {
        contactList = list as MutableList<PhoneContacts>
        contactListFiltered = contactList
        selectedContact = selectedItems
        selectedContactFiltered = selectedContact
        notifyItemRangeChanged(0, list.size)
    }

    val filteredSelected = SparseBooleanArray()

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val searchString = p0 ?: ""

                var filteredList = mutableListOf<PhoneContacts>()

                if (searchString.isEmpty()) {
                    filteredList = contactList
                    selectedContactFiltered = selectedContact
                } else {
                    contactList.filter {
                        (it.name?.contains(searchString, true)!!)
                    }.forEachIndexed { index, it ->
                        if (it.isSelected)
                            filteredSelected.put(index, true)
                        filteredList.add(it)
                    }
                    contactListFiltered = filteredList
                    selectedContactFiltered = filteredSelected
                }

                return FilterResults().apply { values = filteredList }
            }

            override fun publishResults(p0: CharSequence?, results: FilterResults?) {
                contactListFiltered = if (results?.values == null) {
                    mutableListOf()
                } else {
                    results.values as MutableList<PhoneContacts>
                }
                notifyDataSetChanged()
            }

        }
    }


}