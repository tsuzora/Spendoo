package com.example.spendoov2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spendoov2.ui.theme.GreenDark
import com.example.spendoov2.ui.theme.MainBackgroundColor

// A sealed class to represent the different states of our search result
sealed class SearchState {
    object Idle : SearchState() // Default state, nothing searched yet
    data class Found(val pageName: String, val route: String) : SearchState()
    object NotFound : SearchState()
}

@Composable
fun SearchPage(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }
    val recentSearches = remember { mutableStateListOf<String>() }
    val focusManager = LocalFocusManager.current
    var searchState by remember { mutableStateOf<SearchState>(SearchState.Idle) }

    // Define page suggestions and their navigation routes
    val suggestions = remember {
        mapOf(
            "Home" to "home_screen",
            "Analyze" to "statistics_screen",
            "Add Transaction" to "add_screen/new"
        )
    }

    val onSearch = { query: String ->
        if (query.isNotBlank()) {
            if (!recentSearches.contains(query)) {
                recentSearches.add(0, query) // Add to recent searches
            }
            // Check if the search query matches a suggestion
            val match = suggestions.entries.find { (pageName, _) ->
                pageName.equals(query, ignoreCase = true)
            }
            searchState = if (match != null) {
                SearchState.Found(match.key, match.value)
            } else {
                SearchState.NotFound
            }
        }
        focusManager.clearFocus() // Hide keyboard
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MainBackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp, 32.dp)
                .weight(1f)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(R.drawable.arrow_side_line_left),
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp).clickable { navController.popBackStack() }
                )
                LogoContainer(TextStyle(textAlign = TextAlign.Right, color = Color.White, fontSize = 16.sp))
            }
            Spacer(modifier = Modifier.height(16.dp))

            SearchBar(
                value = searchText,
                onValueChange = { newText ->
                    searchText = newText
                    // Reset the search result when the user starts typing again
                    if (searchState !is SearchState.Idle) {
                        searchState = SearchState.Idle
                    }
                },
                onSearch = onSearch,
                focusManager = focusManager
            )

            Spacer(modifier = Modifier.height(24.dp))

            Suggestions(
                suggestions = suggestions.keys.toList(),
                onSuggestionClick = { pageName ->
                    val route = suggestions[pageName]
                    if (route != null) {
                        navController.navigate(route)
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Conditionally display either recent searches or the search result
            if (searchState is SearchState.Idle) {
                RecentSearches(
                    searches = recentSearches,
                    onClear = { recentSearches.clear() },
                    onItemClick = { query ->
                        searchText = query
                        onSearch(query) // Perform a search when a recent item is clicked
                    }
                )
            } else {
                SearchResult(
                    state = searchState,
                    onNavigate = { route ->
                        navController.navigate(route)
                    }
                )
            }
        }
    }
}

@Composable
fun Suggestions(
    suggestions: List<String>,
    onSuggestionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Suggestions:",
            color = Color.White,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            suggestions.forEach { suggestion ->
                Text(
                    text = suggestion,
                    color = Color.White,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onSuggestionClick(suggestion) }
                        .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun SearchResult(
    state: SearchState,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (state) {
            is SearchState.Found -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(GreenDark.copy(alpha = 0.2f))
                        .border(1.dp, GreenDark, RoundedCornerShape(8.dp))
                        .clickable { onNavigate(state.route) }
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text("Result found:", color = Color.Gray, fontSize = 12.sp)
                            Text(state.pageName, color = Color.White, fontSize = 18.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                        }
                        Image(
                            painter = painterResource(id = R.drawable.arrow_side_line_right),
                            contentDescription = "Go to ${state.pageName}",
                            colorFilter = ColorFilter.tint(Color.White),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
            SearchState.NotFound -> {
                Text(
                    text = "Oops, there is nothing there",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
            SearchState.Idle -> {
                // Do nothing in this composable for the Idle state
            }
        }
    }
}

// (Your SearchBar and RecentSearches composables from the previous step go here without changes)
@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            color = GreenDark,
            fontSize = 16.sp,
        ),
        keyboardOptions = KeyboardOptions(
            // Show a "Search" button on the keyboard
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            // Handle the search action
            onSearch = {
                onSearch(value)
            }
        ),
        singleLine = true,
        cursorBrush = SolidColor(GreenDark),
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFD9D9D9)),
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.search_icon),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(GreenDark),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Box(modifier = Modifier.weight(1f)) {
                    // Show placeholder when the text is empty
                    if (value.isEmpty()) {
                        Text(
                            text = "Search",
                            color = GreenDark.copy(alpha = 0.5f),
                            fontSize = 16.sp
                        )
                    }
                    // This is where the user's typing appears
                    innerTextField()
                }
            }
        }
    )
}

@Composable
fun RecentSearches(
    searches: List<String>,
    onClear: () -> Unit,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent:",
                color = Color.White,
                fontSize = 16.sp
            )
            // Show "Clear all" button only if there are recent searches
            if (searches.isNotEmpty()) {
                Text(
                    text = "Clear all",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onClear() }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (searches.isEmpty()) {
            Text(
                text = "No recent searches",
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                textAlign = TextAlign.Center
            )
        } else {
            // Use LazyColumn for better performance with long lists
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(searches) { query ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onItemClick(query) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.icone_close),                          contentDescription = "Recent search icon",
                            colorFilter = ColorFilter.tint(Color.Gray),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = query,
                            color = Color.White,
                            fontSize = 15.sp
                        )
                    }
                    Divider(color = Color.Gray.copy(alpha = 0.3f))
                }
            }
        }
    }
}