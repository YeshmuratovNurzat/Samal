package kz.fime.samal.utils.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kz.fime.samal.utils.binding.BindingRvAdapter

fun<M: Any, V: ViewBinding> AppCompatActivity.setUpAdapter(
    rv: RecyclerView,
    liveItems: LiveData<List<M>>,
    adapter: BindingRvAdapter<M, V>
){
    setUpAdapter(adapter, rv, liveItems, this)
}

fun<M: Any, V: ViewBinding> Fragment.setUpAdapter(
    rv: RecyclerView,
    liveItems: LiveData<List<M>>,
    adapter: BindingRvAdapter<M, V>
){
    setUpAdapter(adapter, rv, liveItems, viewLifecycleOwner)
}

private fun<M: Any, V: ViewBinding> setUpAdapter(adapter: BindingRvAdapter<M, V>, rv: RecyclerView, liveItems: LiveData<List<M>>, lifecycleOwner: LifecycleOwner){
    rv.adapter = adapter
    liveItems.observe(lifecycleOwner) {
        adapter.submitList(it)
    }
}