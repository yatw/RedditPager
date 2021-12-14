package com.example.redditpager.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


/**
 * This class represents a user post in a specific Subreddit,
 * this class can be replied to, in a form of a comment section.
 *
 * The user submission can be a text post, a link to a Url or can include images/media.
 *
 * @property id This item identifier, e.g. "8xwlg"
 *
 * @property fullname Fullname of sumbission, e.g. "t1_c3v7f8u"
 *
 * @property allAwarding List of the submission's received awardings.
 *
 * @property author Name of the Submission's author.
 *
 * @property authorFlairBackgroundColor background color of the author's flair, used in the site UI.
 *
 * @property authorFlairCssClass predefined css class of the author's flair, used in the site UI.
 *
 * @property authorFlairText plain text of the author's flair.
 *
 * @property authorFlairTextColor text color of the author's flair, used in the site UI.
 *
 * @property authorFlairTemplateId template id of the author's flair, used in the site UI.
 *
 * @property authorFlairType type of author flair
 *
 * @property thumbnailUrl the url of the thumbnail image, given there is one.
 *
 * @property thumbnailWidth the width of the thumbnail image, given there is one.
 *
 * @property thumbnailHeight the height of the thumbnail image, given there is one.
 *
 * @property title the title of this submission.
 *
 */
@JsonClass(generateAdapter = true)
data class Submission(

    @Json(name = "id")
    val id: String?,

    @Json(name = "name")
    val fullname: String,

    @Json(name = "author")
    val author: String,


    @Json(name = "author_fullname")
    val authorFullname: String?,

    @Json(name = "clicked")
    val clicked: Boolean,

    @Json(name = "created")
    val created: Long,

    @Json(name = "created_utc")
    val createdUtc: Long,

    @Json(name = "crosspost_parent")
    val crosspostParentFullname: String?,

    @Json(name = "crosspost_parent_list")
    val crosspostParentList: List<Submission>?,

    @Json(name = "distinguished")
    val distinguishedRaw: String?,

    @Json(name = "domain")
    val domain: String,


    @Json(name = "link_flair_background_color")
    val linkFlairBackgroundColor: String?,

    @Json(name = "link_flair_css_class")
    val linkFlairCssClass: String?,

    @Json(name = "link_flair_text")
    val linkFlairText: String?,

    @Json(name = "link_flair_text_color")
    val linkFlairTextColor: String?,

    @Json(name = "link_flair_template_id")
    val linkFlairTemplateId: String?,

    @Json(name = "link_flair_type")
    val linkFlairType: String?,

    @Json(name = "archived")
    val isArchived: Boolean,

    @Json(name = "is_crosspostable")
    val isCrosspostable: Boolean,

    @Json(name = "is_gallery")
    val isGallery: Boolean?,

    @Json(name = "hidden")
    val isHidden: Boolean,

    @Json(name = "locked")
    val isLocked: Boolean,

    @Json(name = "media_only")
    val isMediaOnly: Boolean,

    @Json(name = "is_meta")
    val isMeta: Boolean,

    @Json(name = "pinned")
    val isPinned: Boolean,

    @Json(name = "quarantine")
    val isQuarantined: Boolean,

    @Json(name = "is_reddit_media_domain")
    val isRedditMediaDomain: Boolean,

    @Json(name = "is_robot_indexable")
    val isRobotIndexable: Boolean,

    @Json(name = "saved")
    val isSaved: Boolean,

    @Json(name = "is_self")
    val isSelf: Boolean,

    @Json(name = "spoiler")
    val isSpoiler: Boolean,

    @Json(name = "stickied")
    val isStickied: Boolean,

    @Json(name = "is_video")
    val isVideo: Boolean,

    @Json(name = "likes")
    val likes: Boolean?,

    @Json(name = "num_crossposts")
    val numCrossposts: Int,

    @Json(name = "num_comments")
    val numComments: Int,

    @Json(name = "over_18")
    val over18: Boolean,

    @Json(name = "permalink")
    val permalink: String,

    @Json(name = "removed_by_category")
    val removedByCategory: String?,

    @Json(name = "score")
    val score: Int,

    @Json(name = "selftext")
    val selfText: String?,

    @Json(name = "selftext_html")
    val selfTextHtml: String?,

    @Json(name = "subreddit")
    val subreddit: String,

    @Json(name = "subreddit_id")
    val subredditId: String,

    @Json(name = "subreddit_name_prefixed")
    val subredditNamePrefixed: String,

    @Json(name = "subreddit_subscribers")
    val subredditSubscribers: Int,

    @Json(name = "thumbnail")
    val thumbnailUrl: String?,

    @Json(name = "thumbnail_width")
    val thumbnailWidth: Int?,

    @Json(name = "thumbnail_height")
    val thumbnailHeight: Int?,

    @Json(name = "title")
    val title: String,

    @Json(name = "upvote_ratio")
    val upvoteRatio: Float?,

    @Json(name = "ups")
    val ups: Int,

    @Json(name = "url")
    val url: String?
)