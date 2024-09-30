package com.shinlee.showplus.ui.screens.show.mapping

import com.shinlee.network.model.LoginResponse
import com.shinlee.repository.model.LoginDataRep
import com.shinlee.repository.model.VideoInfo
import com.shinlee.showplus.ui.screens.authentication.login.LoginDataUI
import com.shinlee.showplus.ui.screens.authentication.login.UserInfoUI
import com.shinlee.showplus.ui.screens.show.VideoShow

fun List<VideoInfo>.toVideoShowList(): List<VideoShow> {
    return this.map {
        VideoShow(
            id = it.id ?: "",
            videoLink = it.videoUrl ?: "",
            thumbnail = it.thumbnailUrl ?: ""
        )
    }
}

fun LoginDataRep.toLoginDataUI(): LoginDataUI {
    return LoginDataUI(
        token = this.token,
        userInfo = UserInfoUI(
            id = this.userInfo.id,
            email = this.userInfo.email
        )
    )
}