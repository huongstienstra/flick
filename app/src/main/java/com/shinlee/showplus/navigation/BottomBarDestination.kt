package com.shinlee.showplus.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.shinlee.common.R as R

enum class BottomBarDestination(
    val route: String,
    @StringRes val title: Int,
    @DrawableRes val icon: Int
) {

    SHOW(
        route = Routes.SHOW,
        title = R.string.home,
        icon = R.drawable.ic_home,
    ),

    CONTEST(
        route = Routes.CONTEST,
        title = R.string.contest,
        icon = R.drawable.ic_friends,
    ),

    UPLOAD(
        route = Routes.UPLOAD,
        title = R.string.upload,
        icon = R.drawable.ic_friends,
    ),

    SEARCH(
        route = Routes.SEARCH,
        title = R.string.search,
        icon = R.drawable.ic_friends,
    ),

    PROFILE(
        route = Routes.PROFILE,
        title = R.string.profile,
        icon = R.drawable.ic_friends,
    ),

}