package com.shinlee.repository.mapping

//fun List<VideoInfo>.toVideoShowList(): List<VideoShow> {
//    return this.map { parser ->
//        VideoShow(
//            id = parser.id ?: -1,
//            description = parser.description,
//            videoUrl = parser.videoUrl,
//            thumbnailUrl = parser.thumbnailUrl,
//            voteCount = parser.voteCount ?: 0,
//            commentCount = parser.commentCount ?: 0,
//            profileId = parser.profileId,
//            profilePhoto = parser.profilePhoto,
//            profileNickname = parser.profileNickname ?: "",
//            contestId = parser.contestId,
//            contestTitle = parser.contestTitle,
//            contestImage = parser.contestImage,
//            tags = parser.tags?.toTagList(),
//            isFavourite = parser.isFavourite == 1
//        )
//    }
//}
//
//fun List<VideoInfo.Tag>.toTagList(): List<VideoShow.Tag> {
//    return this.map { tag ->
//        VideoShow.Tag(
//            id = tag.id,
//            content = tag.content
//        )
//    }
//}
