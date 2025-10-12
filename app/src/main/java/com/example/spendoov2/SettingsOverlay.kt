package com.example.spendoov2

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.spendoov2.ui.theme.interFamily

@Composable
fun SettingsOverlay(
    onDismiss: () -> Unit,
    onExportClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    // Menggunakan Dialog untuk membuat overlay yang proper
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        // Box semi-transparan yang menutupi seluruh layar
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onDismiss)
        ) {
            // Konten menu, diposisikan di kanan atas
            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 80.dp, end = 24.dp)
                    .width(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .clickable(onClick = {}) // Mencegah klik di dalam menu menutup dialog
                    .padding(16.dp)
            ) {
                SettingsMenuItem("Export Transactions", onExportClick)
                Spacer(modifier = Modifier.height(8.dp))
                SettingsMenuItem("Edit Profile", onEditProfileClick)
                Spacer(modifier = Modifier.height(8.dp))
                SettingsMenuItem("Logout", onLogoutClick)
            }
        }
    }
}

@Composable
private fun SettingsMenuItem(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontFamily = interFamily,
        color = Color.Black,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
    )
}

@Composable
fun ExportOptionsOverlay(
    onDismiss: () -> Unit,
    onAllTransactionsClick: () -> Unit,
    onDailyClick: () -> Unit,
    onMonthlyClick: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .width(280.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(24.dp)
        ) {
            ExportMenuItem("All Transactions", onAllTransactionsClick)
            ExportMenuItem("Daily", onDailyClick)
            ExportMenuItem("Monthly", onMonthlyClick)
        }
    }
}

@Composable
private fun ExportMenuItem(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontFamily = interFamily,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(vertical = 12.dp)
    )
}


@Composable
fun ConfirmationOverlay(
    message: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    Dialog(onDismissRequest = onCancel) {
        Box(
            modifier = Modifier
                .width(280.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = message,
                    fontSize = 16.sp,
                    fontFamily = interFamily,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ConfirmationButton("Yes", Color(0xFFA8D5A8), onConfirm)
                    ConfirmationButton("No", Color(0xFFE5A5A5), onCancel)
                }
            }
        }
    }
}

@Composable
private fun RowScope.ConfirmationButton(
    text: String,
    color: Color,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontFamily = interFamily,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
    }
}


// Settings Overlay
//@Composable
//fun SettingsOverlay(
//    onDismiss: () -> Unit,
//    onExportClick: () -> Unit,
//    onEditProfileClick: () -> Unit,
//    onLogoutClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//
//        Box(
//            modifier = Modifier
//
//                .offset(x = (-24).dp, y = 80.dp)
//                .width(200.dp)
//                .clip(RoundedCornerShape(16.dp))
//                .background(Color.White)
//                .clickable(onClick = {})
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            ) {
//                // Close button
//                Box(
//                    modifier = Modifier.fillMaxWidth()
//                )
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Export Transactions
//                Text(
//                    text = "Export Transactions",
//                    fontSize = 14.sp,
//                    fontFamily = interFamily,
//                    color = Color.Black,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable { onExportClick() }
//                        .padding(vertical = 8.dp)
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                // Edit Profile
//                Text(
//                    text = "Edit Profile",
//                    fontSize = 14.sp,
//                    fontFamily = interFamily,
//                    color = Color.Black,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable { onEditProfileClick() }
//                        .padding(vertical = 8.dp)
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                // Logout
//                Text(
//                    text = "Logout",
//                    fontSize = 14.sp,
//                    fontFamily = interFamily,
//                    color = Color.Black,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable { onLogoutClick() }
//                        .padding(vertical = 8.dp)
//                )
//            }
//        }
//    }
//
//
//// Export Options Overlay
//@Composable
//fun ExportOptionsOverlay(
//    onDismiss: () -> Unit,
//    onAllTransactionsClick: () -> Unit,
//    onDailyClick: () -> Unit,
//    onMonthlyClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Box(
//        modifier = modifier
//            .fillMaxSize()
//            .background(Color.Black.copy(alpha = 0.5f))
//            .clickable(onClick = onDismiss)
//    ) {
//        Box(
//            modifier = Modifier
//                .align(Alignment.TopEnd)
//                .offset(x = (-224).dp, y = 80.dp)
//                .width(180.dp)
//                .clip(RoundedCornerShape(16.dp))
//                .background(Color.White)
//                .clickable(onClick = {})
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            ) {
//                // Close button
//                Box(
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Title
//                Text(
//                    text = "All Transactions",
//                    fontSize = 16.sp,
//                    fontFamily = interFamily,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.Black,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable { onAllTransactionsClick() }
//                        .padding(vertical = 12.dp)
//                )
//
//                // Daily
//                Text(
//                    text = "Daily",
//                    fontSize = 16.sp,
//                    fontFamily = interFamily,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.Black,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable { onDailyClick() }
//                        .padding(vertical = 12.dp)
//                )
//
//                // Monthly
//                Text(
//                    text = "Monthly",
//                    fontSize = 16.sp,
//                    fontFamily = interFamily,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.Black,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable { onMonthlyClick() }
//                        .padding(vertical = 12.dp)
//                )
//            }
//        }
//    }
//}
//
//// Logout Confirmation Overlay
//@Composable
//fun ConfirmationOverlay(
//    title: String = "Confirmation",
//    message: String = "Are you sure?",
//    onConfirm: () -> Unit,
//    onCancel: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Box(
//        modifier = modifier
//            .fillMaxSize()
//            .background(Color.Black.copy(alpha = 0.5f))
//            .clickable(onClick = {})
//    ) {
//        Box(
//            modifier = Modifier
//                .align(Alignment.Center)
//                .width(280.dp)
//                .clip(RoundedCornerShape(16.dp))
//                .background(Color.White)
//                .padding(24.dp)
//        ) {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                // Title (optional, based on image it seems there's a title)
//                Text(
//                    text = message,
//                    fontSize = 16.sp,
//                    fontFamily = interFamily,
//                    color = Color.Black,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier.padding(bottom = 24.dp)
//                )
//
//                // Buttons
//                Row(
//                    horizontalArrangement = Arrangement.spacedBy(16.dp),
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    // Yes button
//                    Box(
//                        modifier = Modifier
//                            .weight(1f)
//                            .height(48.dp)
//                            .clip(RoundedCornerShape(8.dp))
//                            .background(Color(0xFFA8D5A8))
//                            .clickable { onConfirm() },
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = "Yes",
//                            fontSize = 16.sp,
//                            fontFamily = interFamily,
//                            fontWeight = FontWeight.SemiBold,
//                            color = Color.Black
//                        )
//                    }
//
//                    // No button
//                    Box(
//                        modifier = Modifier
//                            .weight(1f)
//                            .height(48.dp)
//                            .clip(RoundedCornerShape(8.dp))
//                            .background(Color(0xFFE5A5A5))
//                            .clickable { onCancel() },
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = "No",
//                            fontSize = 16.sp,
//                            fontFamily = interFamily,
//                            fontWeight = FontWeight.SemiBold,
//                            color = Color.Black
//                        )
//                    }
//                }
//            }
//        }
//    }
//}

@Preview
@Composable
fun SettingsOverlayPreview() {
    SettingsOverlay(
        onDismiss = {},
        onExportClick = {},
        onEditProfileClick = {},
        onLogoutClick = {}
    )
}

