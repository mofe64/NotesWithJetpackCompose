package com.nubari.notes.feature_note.presentation.notes.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.nubari.notes.feature_note.domain.model.Note

@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier,
    cornerRadius: Dp = 10.dp,
    cutCornerSize: Dp = 30.dp,
    onDeleteClick: () -> Unit,
) {
    Box(modifier = modifier) {
        /**
         * Canvas composable need a fixed size,
         * They need to know the size, when they are being initialized
         * This is why we use the match parent size,  it gives us the size after
         * the parent has measure it's constraints
         * */
        Canvas(
            modifier = Modifier.matchParentSize(),
        ) {
            /**
             * Our path starts at point 0,0
             * Note our size here refers to the size of the canvas and we have
             * access to it inside the scope
             * */
            val clipPath = Path().apply {
                /**we move to this point along the x axis
                 * Because we want tp have a cut off at the right hand corner,
                 * we stop our line just before the cutCornerSize value on the x axis
                 * and not the full width
                 * **/
                lineTo(size.width - cutCornerSize.toPx(), 0f)
                /**
                 * Because we are moving along the x axis and incrementing the y
                 * Which was previously 0 at the same
                 * time we get a slope from out previous stop to the current stop
                 * */
                lineTo(size.width, cutCornerSize.toPx())
                /**
                 * On the same x axis, we move down to the size.height value
                 * **/
                lineTo(size.width, size.height)
                /**
                 * We move back along the x axis
                 * **/
                lineTo(0f, size.height)
                close()
            }
            /**
             * We want to clip a path on our current path, which we created above
             * **/
            clipPath(clipPath) {
                drawRoundRect(
                    color = Color(note.color),
                    size = size,
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
                drawRoundRect(
                    color = Color(
                        ColorUtils.blendARGB(note.color, 0x000000, 0.2f)
                    ),
                    topLeft = Offset(size.width - cutCornerSize.toPx(), -100f),
                    size = Size(cutCornerSize.toPx() + 100f, cutCornerSize.toPx() + 100f),
                    cornerRadius = CornerRadius(cornerRadius.toPx())
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start=16.dp,end = 32.dp, bottom = 16.dp)
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.content,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
                maxLines = 10,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colors.onSurface
            )
        }
    }
}