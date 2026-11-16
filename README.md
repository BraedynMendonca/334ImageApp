# Project 3.3.4: Image App

This project uses two-dimensional `Pixel` arrays to transform `lib/beach.jpg` and add `lib2/balloon.png`. The eight results are saved in `output/`; `output/results-overview.png` presents them together for review.

## Run

Open this folder in an IDE with a Java JDK installed and run `ImageApp.java` with the working directory set to this folder. The program saves eight PNG files in `output/` and opens each result in an image explorer when a graphical desktop is available. From a terminal in this folder:

```sh
javac *.java
java ImageApp
```

For a machine without a graphical desktop, use `java -Djava.awt.headless=true ImageApp` to save the same files without opening windows. The output files can be opened separately for screenshots.

## Results

1. Recolored: changes RGB to blue, red, green.
2. Negative: subtracts each color channel from 255.
3. Grayscale: replaces all three channels with their integer average.
4. 180-degree rotation.
5. 90-degree clockwise rotation.
6. 270-degree clockwise rotation.
7. Composite: places the balloon on the beach, skipping white pixels and respecting transparency.
8. Blended composite: blends the balloon with the beach at half opacity.

The rotations use `Vector1by2` and `Matrix2by2` multiplication and a new array of the right dimensions, preserving the whole rectangular image. The composite checks the destination bounds, so an overlay can be partly outside the background without causing an error.

## Evaluation and discussion

The generated images were checked against the color and rotation formulas. The composite was checked to ensure it changes the background while leaving white background pixels unchanged. All eight results were produced successfully. Development time estimates and actual time should be filled in by the student based on their own experience.

A 2D array fits an image because pixels form a rectangular grid and can be accessed directly by row and column. With differently sized images, an overlay can extend beyond the background, be too large to fit, or cover important content; checking bounds avoids an indexing error, and choosing a suitable position or resizing addresses the visual issues.

Image editing can improve clarity or create art, but an altered image can also mislead people, harm reputations, or distort evidence. Context matters: news, scientific, and legal images should disclose meaningful alterations and maintain access to the original when appropriate. Creative edits can be more flexible, but creators should still avoid presenting a fabricated scene as a real event.

## Matrix tests

Run `java MatrixTests` after compiling. This checks vector access, dot products, matrix access, multiplication, rotations, and invalid indexing. An additional general matrix multiplication example and tests are in `MatrixMath/` and can be run with `javac MatrixMath/*.java` followed by `java -cp MatrixMath MatrixMathTest`.

Project management materials are in `management/Team-and-Management.md`. The team should fill in actual roles, dates, work logs and agreed norms.
