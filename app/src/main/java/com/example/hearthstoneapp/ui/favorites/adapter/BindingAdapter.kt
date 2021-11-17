package com.example.hearthstoneapp.ui.favorites.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.hearthstoneapp.R
import com.example.hearthstoneapp.data.database.FavoriteCard
import com.example.hearthstoneapp.data.network.model.SearchResponse

@BindingAdapter("cName", "fName")
fun bindFName(textView: TextView, CName: String?, fName: String?) {
    if (CName != null) textView.text = CName
    else textView.text = fName
}

@BindingAdapter("cRarity", "fRarity")
fun bindFRarity(textView: TextView, cRarity: String?, fRarity: String?) {
    if (cRarity != null) textView.text = cRarity
    else textView.text = fRarity
}

@BindingAdapter("cType", "fType")
fun bindFType(textView: TextView, cType: String?, fType: String?) {
    if (cType != null) textView.text = cType
    else textView.text = fType
}

@BindingAdapter("cCardSet", "fCardSet")
fun bindFCardSet(textView: TextView, cCardSet: String?, fCardSet: String?) {
    if (cCardSet != null) textView.text = cCardSet
    else textView.text = fCardSet
}

@BindingAdapter("cImgUrl", "fImgUrl")
fun bindFImage(imgView: ImageView, cImgUrl: String?, fImgUrl: String?) {
    if (null != cImgUrl) {
        val imgUri = cImgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imgView)
    } else if (null != fImgUrl) {
        val imgUri = fImgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imgView)
    } else {
        imgView.setImageResource(R.drawable.no_image)
    }
}

@BindingAdapter("card")
fun bindVisibility(imgView: ImageView, card: SearchResponse?){
    if (card != null) imgView.visibility = View.VISIBLE
    else imgView.visibility = View.GONE
}

@BindingAdapter("favorite")
fun bindVisibilityF(imgView: ImageView, favorite: FavoriteCard?){
    if (favorite != null) imgView.visibility = View.VISIBLE
    else imgView.visibility = View.GONE
}
