package com.enestekin.service

import com.enestekin.data.models.Comment
import com.enestekin.data.repository.comment.CommentRepository
import com.enestekin.data.repository.user.UserRepository
import com.enestekin.data.requests.CreateCommentRequest
import com.enestekin.data.responses.CommentResponse
import com.enestekin.util.Constants.MAX_COMMENT_LENGTH
class CommentService(
    private val commentRepository: CommentRepository,
    private val  userRepository: UserRepository
) {

    suspend fun createComment(createCommentRequest: CreateCommentRequest, userId: String): ValidationEvent {
        createCommentRequest.apply {
            if(comment.isBlank() || postId.isBlank()) {
                return ValidationEvent.ErrorFieldEmpty
            }
            if(comment.length > MAX_COMMENT_LENGTH) {
                return ValidationEvent.ErrorCommentTooLong
            }
        }
        val user = userRepository.getUserById(userId) ?: return ValidationEvent.UserNotFound
        commentRepository.createComment(
            Comment(
                username = user.username,
                profileImageUrl = user.profileImageUrl ,
                likeCount = 0,
                comment = createCommentRequest.comment,
                userId = userId,
                postId = createCommentRequest.postId,
                timestamp = System.currentTimeMillis()
            )
        )
        return ValidationEvent.Success
    }

    suspend fun deleteCommentsForPost(postId: String) {
        commentRepository.deleteCommentsFromPost(postId)
    }

    suspend fun deleteComment(commentId: String): Boolean {
        return commentRepository.deleteComment(commentId)
    }

    suspend fun getCommentsForPost(postId: String, ownUserId: String): List<CommentResponse> {
        return commentRepository.getCommentsForPost(postId,ownUserId)
    }

    suspend fun getCommentById(commentId: String): Comment? {
        return commentRepository.getComment(commentId)
    }

    sealed class ValidationEvent {
        object ErrorFieldEmpty : ValidationEvent()
        object ErrorCommentTooLong : ValidationEvent()
        object UserNotFound: ValidationEvent()
        object Success : ValidationEvent()
    }
}