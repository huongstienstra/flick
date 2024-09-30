package com.shinlee.showplus.ui.screens.show.mapping

import com.shinlee.repository.model.LoginEntity
import com.shinlee.repository.model.VideoInfo
import com.shinlee.showplus.ui.screens.authentication.login.LoginData
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

fun LoginEntity.toLoginData(): LoginData {
    return LoginData(
        token = this.token,
        userInfo = LoginData.UserInfo(
            id = this.userInfo.id,
            email = this.userInfo.email
        )
    )
}