package com.example.hearthstoneapp.ui.carddetails.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.hearthstoneapp.R

@BindingAdapter("type")
fun bindType(textView: TextView, type: String?){
    type?.let {
        textView.text = "Type: $type"
    }
}

@BindingAdapter("rarity")
fun bindRarity(textView: TextView, rarity: String?){
    rarity?.let {
        textView.text = "Rarity: $rarity"
    }
}

@BindingAdapter("set")
fun bindSet(textView: TextView, set: String?){
    set?.let {
        textView.text = "Set: $set"
    }
}

@BindingAdapter("effect")
fun bindEffect(textView: TextView, effect: String?){
    effect?.let {
        textView.text = "Effect: $effect"
    }
}

@BindingAdapter("imgUrl")
fun bindImg(imgView: ImageView, imgUrl: String?){
    if (null != imgUrl) {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image))
            .into(imgView)
    }else {
        imgView.setImageResource(R.drawable.no_image)
    }
}