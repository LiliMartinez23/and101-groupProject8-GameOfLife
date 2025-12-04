package com.example.gameoflife

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.gameoflife.ui.theme.GameOfLifeTheme
import kotlinx.coroutines.delay
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream


class MainActivity : ComponentActivity() {
    override fun onCreate( savedInstanceState: Bundle? ) {
        super.onCreate( savedInstanceState )
        setContent {
            GameOfLifeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GameOfLifeApp()
                }
            }
        }
    }
}

@Composable
fun GameOfLifeApp() {
    var showRules by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer( modifier = Modifier.height(50.dp))
        // Rules button
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Button( onClick = { showRules = true } ) {
                Text("Rules")
            }
        }
        // Title of game
        Text(
            text = "Conway's Game of Life",
            fontSize = 30.sp,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 30.dp)
        )
        Spacer( modifier = Modifier.height(30.dp))

        // Game
        GameOfLifeScreen()
    }

    // Rules modal
    if (showRules) {
        AlertDialog(
            onDismissRequest = { showRules = false },
            confirmButton = {
                TextButton( onClick = { showRules = false } ) {
                    Text("Close")
                }
            },
            title = { Text( "How to play") },
            text = {
                RulesContent()
            }
        )
    }
}

// Rules
@Composable
fun RulesContent() {
    // Rules
    Column( horizontalAlignment = Alignment.CenterHorizontally ) {
        Spacer( modifier = Modifier.height(20.dp) )
        Text("1. Underpopulation: Any live cell with fewer than two live neighbors dies.",
            modifier = Modifier.padding(horizontal = 15.dp))
            Spacer( modifier = Modifier.height(5.dp) )
        Text("2. Overpopulation: Any live cell with more than three live neighbors dies.",
            modifier = Modifier.padding(horizontal = 15.dp))
            Spacer( modifier = Modifier.height(5.dp) )
        Text("3. Birth: Any dead cell with exactly three live neighbors becomes a live cell.",
            modifier = Modifier.padding(horizontal = 15.dp))
            Spacer( modifier = Modifier.height(5.dp) )
        Text("4. Survival: Any live cell with two or three live neighbors lives on to the next generation.",
            modifier = Modifier.padding(horizontal = 15.dp))
    }
}

@Composable
// Display game on screen
fun GameOfLifeScreen( rows: Int = 15, cols: Int = 15 ) { // size of grid
    val context = LocalContext.current

    // Grid state
    var grid by remember { mutableStateOf(generateEmptyGrid( rows, cols )) }

    // App is running
    var isRunning by remember { mutableStateOf( false ) }

    // Automate next generation process
    LaunchedEffect( isRunning ) {
        while ( isRunning ) {
            grid = nextGeneration( grid )
            // Adjust speed (ms)
            delay( 200L )
        }
    }

    Column( horizontalAlignment = Alignment.CenterHorizontally ) {
        // Grid
        Column {
            for ( r in 0 until rows ) {
                Row {
                    for ( c in 0 until cols ) {
                        val isAlive = grid[r][c]
                        Box(
                            modifier = Modifier
                                .size(22.dp) // size of cells
                                .padding(1.dp)
                                .background(
                                    // isAlive is true, change color of cell to yellow
                                    if ( isAlive ) Color.Yellow else Color.LightGray
                                )
                                .clickable {
                                    grid = toggleCell( grid, r, c )
                                }
                        )
                    }
                }
            }
        }
        Spacer( modifier = Modifier.height(50.dp) )
        // Buttons
        Column( horizontalAlignment = Alignment.CenterHorizontally ) {
            Row( horizontalArrangement = Arrangement.spacedBy(8.dp) ) {
                // Start button
                Button(
                    onClick = { isRunning = !isRunning },
                    modifier = Modifier.size(width = 150.dp, height = 60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF22ec51),
                        contentColor = Color.Black
                    )
                ) {
                    Text( "Start", fontSize = 20.sp )
                }
                // Stop button
                Button(
                    onClick = { isRunning = false },
                    enabled = isRunning,
                    modifier = Modifier.size(width = 150.dp, height = 60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFff3800),
                    )
                ) {
                    Text("Stop", fontSize = 20.sp)
                }
            }

            Spacer( modifier = Modifier.height(8.dp) )

            Row( horizontalArrangement = Arrangement.spacedBy(8.dp) ) {
                // Clear button
                Button(
                    onClick = { grid = generateEmptyGrid(rows, cols) },
                    modifier = Modifier.size(width = 150.dp, height = 60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    border = BorderStroke(2.dp, Color.Black)
                ) {
                    Text("Clear", fontSize = 20.sp)
                }
                // Share button
                Button(
                    onClick = { shareGridImage(context, grid) },
                    // Only share when the simulation has stopped running
                    enabled = !isRunning,
                    modifier = Modifier.size(width = 150.dp, height = 60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF018fed)
                    ),
                ) {
                    Text("Share", fontSize = 20.sp)
                }
            }
        }
    }
}

// Build empty grid
fun generateEmptyGrid( rows: Int, cols: Int ): Array<BooleanArray> =
    Array(rows) { BooleanArray( cols ) { false } }

// Alive or dead
fun toggleCell(
    grid: Array<BooleanArray>,
    row: Int,
    col: Int
): Array<BooleanArray> {
    val newGrid = Array( grid.size ) {
        r -> grid[r].clone()
    }
    newGrid[row][col] = !newGrid[row][col]
    return newGrid
}

//Count live neighbors surrounding the cell (vertical, horizontal, diagonal)
fun countLiveNeighbors(
    grid: Array<BooleanArray>,
    row: Int,
    col: Int
): Int {
    val rows = grid.size
    val cols = grid[0].size
    var count = 0

    for ( dr in -1..1 ) {
        for ( dc in -1..1 ) {
            if ( dr == 0 && dc == 0 ) continue

            val r = row + dr
            val c = col + dc

            if ( r in 0 until rows && c in 0 until cols && grid[r][c] ) {
                count++
            }
        }
    }

    return count
}

// Compute next generation based on Conway's rules
fun nextGeneration( grid: Array<BooleanArray> ): Array<BooleanArray> {
    val rows = grid.size
    val cols = grid[0].size
    val newGrid = Array(rows) { BooleanArray(cols) { false } }

    for ( r in 0 until rows ) {
        for ( c in 0 until cols ) {
            val isAlive = grid[r][c]
            val liveNeighbors = countLiveNeighbors(grid, r, c)

            newGrid[r][c] = when {
                // isAlive true && neighbors < 2 ==> dies (underpopulation)
                isAlive && liveNeighbors < 2 -> false

                // isAlive true && == 2 or == 3 ==> survives
                isAlive && (liveNeighbors == 2 || liveNeighbors == 3) -> true

                // isAlive true && neighbors > 3 ==> dies (overpopulation)
                isAlive && liveNeighbors > 3 -> false

                // isAlive !true && neighbors == 3 ==> born
                !isAlive && liveNeighbors == 3 -> true

                else -> false
            }
        }
    }
    return newGrid
}

// Sharing the image
fun createGridBitmap( grid: Array<BooleanArray> ): Bitmap {
    val rows = grid.size
    val cols = grid[0].size
    val cellSize = 30 // pixels per cell
    val width = cols * cellSize
    val height = rows * cellSize
    val bitmap = Bitmap.createBitmap( width, height, Bitmap.Config.ARGB_8888 )
    val canvas = Canvas(bitmap)
    val paint = Paint()

    for ( r in 0 until rows ) {
        for ( c in 0 until cols ) {
            paint.color = if ( grid[r][c] ) {
                android.graphics.Color.YELLOW // yellow
            } else {
                android.graphics.Color.LTGRAY // light gray
            }
            val left = ( c * cellSize ).toFloat()
            val top = ( r * cellSize ).toFloat()
            val right = left + cellSize
            val bottom = top + cellSize
            canvas.drawRect( left, top, right, bottom, paint )
        }
    }
    return bitmap
}

// Share sheet to share images of grid
fun shareGridImage( context: Context, grid: Array<BooleanArray>) {
    val bitmap = createGridBitmap(grid)

    val cachePath = File( context.cacheDir, "images" )
    cachePath.mkdirs()
    val file = File( cachePath, "game_of_life_grid.png")
    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out )
    }

    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(Intent.createChooser(shareIntent, "Share Game of Life grid"))
}