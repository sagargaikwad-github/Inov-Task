package com.example.inov_task.view

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide

import com.example.inov_task.R
import com.example.inov_task.callback.colorListener
import com.example.inov_task.databinding.ActivityMainBinding

import com.example.inov_task.model.Attribute
import com.example.inov_task.model.ProductInfo
import com.example.inov_task.repository.LensRepository
import com.example.inov_task.viewmodel.LensViewModel
import com.example.inov_task.viewmodel.LensViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), colorListener {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: LensViewModel
    lateinit var viewModelFactory: LensViewModelFactory
    lateinit var repository: LensRepository
    lateinit var viewPagerAdapter: ViewPagerAdapter

    var attributeList: List<Attribute> = ArrayList()
    lateinit var productInfo: ProductInfo
    var imageList = mutableListOf<String>()

    var openDescription = true
    var prevId: String? = null
    var defaultCart = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        //When activity Launch , Data are null so we added Progressbar and layout Visibility are gone.
        binding.progressBar.visibility = View.VISIBLE
        binding.constraint2.visibility = View.GONE


        //Check Internet Connection Not needed , but we not used sealed class for response
        if (isConnectedOrConnecting()) {
            repository = LensRepository()
            viewModelFactory = LensViewModelFactory(repository, defaultCart)

            viewModel = ViewModelProvider(this, viewModelFactory).get(LensViewModel::class.java)


            GlobalScope.launch(Dispatchers.Main) {

                //Getting all reponse in getLenses() function
                viewModel.getLenses().observe(this@MainActivity, Observer {
                    if (it != null) {

                        //Progressbar visibilty gone if response not null.
                        binding.progressBar.visibility = View.GONE
                        binding.constraint2.visibility = View.VISIBLE

                        //ProductInfo is a Object inside LensCollection so accessed from here.
                        productInfo = it.data

                        //We also need AttributeList so that get from productInfo Object
                        for (item in productInfo.configurable_option.listIterator()) {
                            attributeList = item.attributes
                        }

                        //Images for Viewpager, that are under a productInfo model obj.
                        for (i in productInfo.images) {
                            imageList.add(i)
                        }


                        setData()
                        setViewPager()
                        setRecyclerView()

                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Something Went wrong",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                })
            }
        } else {
            Toast.makeText(this, "Internet Connection Error", Toast.LENGTH_SHORT).show()
        }


        Glide.with(this).load(R.drawable.tobby).into(binding.brandCollab)


        //Functina
        addRemoveCart()

        binding.toolbarLayout.actionBarBack.setOnClickListener {
            Toast.makeText(this@MainActivity, "Back Pressed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addRemoveCart() {

        //here we update our textview from ViewModel
        //Not necessary to use this from viewModel , because our activity is portrait only so no Configuration Changes.
        //Also we update background color of minus button if quantity select more than 1.
        binding.apply {
            var getVal: Int
            minusCart.setOnClickListener {
                getVal = viewModel.minusFromCart()
                productsAdded.text = getVal.toString()

                updateMinusButtonColor(getVal)
            }

            addCart.setOnClickListener {
                getVal = viewModel.addToCart()
                productsAdded.text = getVal.toString()

                updateMinusButtonColor(getVal)
            }

        }
    }

    private fun updateMinusButtonColor(getVal: Int) {
        val getGreyColor = resources.getColor(R.color.minus_cart_grey_color)
        val getBlackColor = resources.getColor(R.color.black)

        if (getVal > 1) {
            binding.minusCart.setBackgroundColor(getBlackColor)
        } else {
            binding.minusCart.setBackgroundColor(getGreyColor)
        }
    }


    //Viewpager with imagesList
    private fun setViewPager() {
        binding.apply {
            viewPagerAdapter = ViewPagerAdapter(imageList)
            viewPager2.adapter = viewPagerAdapter
            viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL

            //Setting viewpager-indicator to viewpager
            viewpagerDotsIndicator.attachTo(binding.viewPager2)
        }

    }

    //Rounded ColorChange-LensChange Recyclerview
    private fun setRecyclerView() {
        val recyclerAdapter = colorAdapter(attributeList, this, this)
        binding.colorRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.colorRecyclerView.adapter = recyclerAdapter
        recyclerAdapter.notifyDataSetChanged()
    }

    //Assigning Product data to each view.
    private fun setData() {
        binding.apply {
            toolbarLayout.actionBarTitle.text = productInfo.name

            brandName.text = productInfo.brand_name
            dataName.text = productInfo.name
            price.text = "${productInfo.final_price.toString() + " KWD"}"
            sku.text = "SKU : ${productInfo.sku}"

            var desc = productInfo.description
            // desc.replace("\\r\\n", "")
            description.text = Html.fromHtml(desc)

        }


        //Product Information Open-Close TextView
        binding.openCloseInformation.setOnClickListener {
            //Default openDescription is true means TextView Opened

            if (openDescription) {
                binding.openCloseInformation.setImageResource(R.drawable.ic_down)
                openDescription = false
                binding.description.visibility = View.GONE
            } else {
                openDescription = true
                binding.openCloseInformation.setImageResource(R.drawable.ic_up)
                binding.description.visibility = View.VISIBLE
            }
        }


    }


    //Callback from adapter , if User Click on RecyclerView Item , MainActivity get Notified
    //PrevID is Used for preventing same RecyclerItem Click
    override fun colorChange(optionId: String) {

        //We Compare attribute optionID with User Recyclerview click optionId.
        for (item in attributeList) {

            if (optionId == item.option_id) {
                if (prevId != optionId) {
                    prevId = optionId

                    //removeAll because default lens List need to remove only selected lens list needed.
                    imageList.removeAll(imageList)

                    binding.colorName.text = "Color : ${item.value}"

                    for (url in item.images) {
                        imageList.add(url)
                    }
                    setViewPager()
                }
            }
        }


    }

    //Check Internet Connection only if needed.
    private fun isConnectedOrConnecting(): Boolean {
        val connMgr = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }


}