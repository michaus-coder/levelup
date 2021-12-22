package com.example.levelup

import com.google.firebase.auth.FirebaseUser

data class HistoryEventListItem(
    var id: String,
    var id_event: String,
    var id_user: FirebaseUser?
)
