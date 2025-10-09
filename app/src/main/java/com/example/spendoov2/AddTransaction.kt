import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import com.example.spendoov2.R

@Composable
fun AddTransaction(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .requiredWidth(width = 440.dp)
            .requiredHeight(height = 956.dp)
            .clip(shape = RoundedCornerShape(24.dp))
            .background(
                brush = Brush.linearGradient(
                    0f to Color(0xff1b1c28),
                    1f to Color(0xff178237),
                    start = Offset(143.5f, 223.5f),
                    end = Offset(46f, 0f)
                )
            )
    ) {
        Box(
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .offset(
                    x = 0.dp,
                    y = 0.dp
                )
                .requiredWidth(width = 440.dp)
                .requiredHeight(height = 912.dp)
                .background(
                    brush = Brush.linearGradient(
                        0f to Color(0xff178237),
                        1f to Color(0xff1b1c28),
                        start = Offset(220f, 0f),
                        end = Offset(220f, 819f)
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 26.dp,
                        y = 13.dp
                    )
                    .requiredWidth(width = 388.dp)
                    .requiredHeight(height = 869.dp)
            ) {
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 341.dp,
                            y = 0.dp
                        )
                        .requiredSize(size = 45.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .requiredSize(size = 45.dp)
                            .clip(shape = CircleShape)
                            .background(
                                brush = Brush.radialGradient(
                                    0f to Color(0xffff6a6a),
                                    1f to Color(0xff994040),
                                    center = Offset(22.75f, 22.5f),
                                    radius = 32.5f
                                )
                            )
                    )
                }
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 0.dp,
                            y = 11.dp
                        )
                        .requiredWidth(width = 252.dp)
                        .requiredHeight(height = 25.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.arrow_side_line_left),
                        contentDescription = "Vector",
                        modifier = Modifier
                            .fillMaxSize()
                    )
                    Text(
                        text = "Add Transaction",
                        color = Color.White,
                        lineHeight = 1.em,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .align(alignment = Alignment.TopStart)
                            .offset(
                                x = 54.dp,
                                y = 3.dp
                            )
                            .requiredWidth(width = 198.dp)
                            .requiredHeight(height = 18.dp)
                            .wrapContentHeight(align = Alignment.CenterVertically)
                    )
                }
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 0.dp,
                            y = 62.dp
                        )
                        .requiredWidth(width = 188.dp)
                        .requiredHeight(height = 31.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .requiredWidth(width = 188.dp)
                            .requiredHeight(height = 31.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .requiredWidth(width = 188.dp)
                                .requiredHeight(height = 31.dp)
                                .clip(
                                    shape = RoundedCornerShape(
                                        topStart = 30.dp,
                                        bottomStart = 30.dp
                                    )
                                )
                                .background(color = Color(0xff08584a))
                        )
                        Text(
                            text = "Income",
                            color = Color(0xff178237),
                            lineHeight = 1.em,
                            style = TextStyle(
                                fontSize = 12.419355392456055.sp
                            ),
                            modifier = Modifier
                                .align(alignment = Alignment.TopStart)
                                .offset(
                                    x = 72.dp,
                                    y = 9.dp
                                )
                                .wrapContentHeight(align = Alignment.CenterVertically)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 200.dp,
                            y = 62.dp
                        )
                        .requiredWidth(width = 188.dp)
                        .requiredHeight(height = 31.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .requiredWidth(width = 188.dp)
                            .requiredHeight(height = 31.dp)
                            .clip(shape = RoundedCornerShape(topEnd = 30.dp, bottomEnd = 30.dp))
                            .background(color = Color(0xffacacac))
                    )
                    Text(
                        text = "Expense",
                        color = Color.Black,
                        lineHeight = 1.em,
                        style = TextStyle(
                            fontSize = 12.419355392456055.sp
                        ),
                        modifier = Modifier
                            .align(alignment = Alignment.TopStart)
                            .offset(
                                x = 69.dp,
                                y = 9.dp
                            )
                            .wrapContentHeight(align = Alignment.CenterVertically)
                    )
                }
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 0.dp,
                            y = 62.dp
                        )
                        .requiredWidth(width = 188.dp)
                        .requiredHeight(height = 31.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .requiredWidth(width = 188.dp)
                            .requiredHeight(height = 31.dp)
                            .clip(shape = RoundedCornerShape(topStart = 30.dp, bottomStart = 30.dp))
                            .background(
                                brush = Brush.linearGradient(
                                    0f to Color(0xff2f9944),
                                    1f to Color(0xff54e871),
                                    start = Offset(0f, 15.5f),
                                    end = Offset(188f, 15.5f)
                                )
                            )
                    )
                    Text(
                        text = "Income",
                        color = Color.White,
                        lineHeight = 1.em,
                        style = TextStyle(
                            fontSize = 12.419355392456055.sp
                        ),
                        modifier = Modifier
                            .align(alignment = Alignment.TopStart)
                            .offset(
                                x = 72.dp,
                                y = 9.dp
                            )
                            .wrapContentHeight(align = Alignment.CenterVertically)
                    )
                }
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 200.dp,
                            y = 62.dp
                        )
                        .requiredWidth(width = 188.dp)
                        .requiredHeight(height = 31.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .requiredWidth(width = 188.dp)
                            .requiredHeight(height = 31.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .requiredWidth(width = 188.dp)
                                .requiredHeight(height = 31.dp)
                                .clip(shape = RoundedCornerShape(topEnd = 30.dp, bottomEnd = 30.dp))
                                .background(color = Color(0xffacacac))
                        )
                        Text(
                            text = "Expense",
                            color = Color.Black,
                            lineHeight = 1.em,
                            style = TextStyle(
                                fontSize = 12.419355392456055.sp
                            ),
                            modifier = Modifier
                                .align(alignment = Alignment.TopStart)
                                .offset(
                                    x = 69.dp,
                                    y = 9.dp
                                )
                                .wrapContentHeight(align = Alignment.CenterVertically)
                        )
                    }
                }
                Text(
                    text = "Amount",
                    color = Color.White,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 0.52.em,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Light
                    ),
                    modifier = Modifier
                        .align(alignment = Alignment.TopCenter)
                        .offset(
                            x = 5.dp,
                            y = 190.dp
                        )
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
                Text(
                    text = "Rp",
                    color = Color.White,
                    lineHeight = 0.52.em,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 34.dp,
                            y = 190.dp
                        )
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
                Divider(
                    color = Color.Black,
                    modifier = Modifier
                        .align(alignment = Alignment.TopCenter)
                        .offset(
                            x = 0.5.dp,
                            y = 214.dp
                        )
                        .requiredWidth(width = 323.dp)
                )
                Text(
                    text = "Category",
                    color = Color.White,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 0.52.em,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Light
                    ),
                    modifier = Modifier
                        .align(alignment = Alignment.TopCenter)
                        .offset(
                            x = 6.dp,
                            y = 259.dp
                        )
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
                Divider(
                    color = Color.Black,
                    modifier = Modifier
                        .align(alignment = Alignment.TopCenter)
                        .offset(
                            x = (-0.5).dp,
                            y = 283.dp
                        )
                        .requiredWidth(width = 323.dp)
                )
                Text(
                    text = "Payment Method",
                    color = Color.White,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 0.52.em,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Light
                    ),
                    modifier = Modifier
                        .align(alignment = Alignment.TopCenter)
                        .offset(
                            x = 7.5.dp,
                            y = 328.dp
                        )
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
                Divider(
                    color = Color.Black,
                    modifier = Modifier
                        .align(alignment = Alignment.TopCenter)
                        .offset(
                            x = 1.5.dp,
                            y = 352.dp
                        )
                        .requiredWidth(width = 323.dp)
                )
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 294.dp,
                            y = 779.dp
                        )
                        .requiredSize(size = 90.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .requiredSize(size = 90.dp)
                            .clip(shape = CircleShape)
                            .background(
                                brush = Brush.radialGradient(
                                    0f to Color(0xff53e670),
                                    1f to Color(0xff1b1c28),
                                    center = Offset(45.49f, 45f),
                                    radius = 65f
                                )
                            )
                    )
                    Image(
                        painter = painterResource(id = R.drawable.save_icon),
                        contentDescription = "save_icon",
                        modifier = Modifier
                            .align(alignment = Alignment.TopStart)
                            .offset(
                                x = 20.dp,
                                y = 20.dp
                            )
                            .requiredSize(size = 50.dp)
                            .shadow(elevation = 4.dp)
                    )
                }
            }
            Image(
                painter = painterResource(id = R.drawable.delete_icon),
                contentDescription = "delete_icon",
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 378.dp,
                        y = 23.dp
                    )
                    .requiredWidth(width = 24.dp)
                    .requiredHeight(height = 25.dp)
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 59.dp,
                    y = 173.dp
                )
                .requiredWidth(width = 323.dp)
                .requiredHeight(height = 32.dp)
        ) {
            Calendar()

            Time()
        }
    }
}

@Composable
fun Calendar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .requiredWidth(width = 151.dp)
            .requiredHeight(height = 30.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.calendar_icon),
            contentDescription = "calendar_icon",
            modifier = Modifier
        )
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 38.dp,
                    y = 10.86.dp
                )
                .requiredWidth(width = 113.dp)
                .requiredHeight(height = 13.dp)
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(width = 113.dp)
                    .requiredHeight(height = 13.dp)
            ) {
                Text(
                    text = "August 16, 2025",
                    color = Color.White,
                    lineHeight = 0.89.em,
                    style = TextStyle(
                        fontSize = 14.sp
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
            }
        }
    }
}

@Composable
fun Time(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .requiredWidth(width = 120.dp)
            .requiredHeight(height = 30.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.clock_icon),
            contentDescription = "clock_icon",
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier
        )
        Text(
            text = "12.00 PM",
            color = Color.White,
            lineHeight = 0.89.em,
            style = TextStyle(
                fontSize = 14.sp
            ),
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight(align = Alignment.CenterVertically)
        )
    }
}

@Preview
@Composable
private fun AddTransactionPreview() {
    AddTransaction(Modifier)
}