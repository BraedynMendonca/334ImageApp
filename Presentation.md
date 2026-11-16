# Image App: Presentation

## 1. Goal

Use 2D pixel arrays to change image colors, rotate images, and combine two images. The source is a beach photograph; the added image is a balloon.

## 2. Color transformations

- Recolor: rearrange red, green, and blue to blue, red, and green.
- Negative: subtract each channel from 255.
- Grayscale: assign the average of the three channels to each channel.

Show `output/01-recolored.png`, `output/02-negative.png`, and `output/03-grayscale.png`.

## 3. Rotations

Each original pixel is copied to a new position for a 180°, 90° clockwise, or 270° clockwise turn. For quarter turns, the output width and height switch so the full photo fits.

Show `output/04-rotate-180.png`, `output/05-rotate-90.png`, and `output/06-rotate-270.png`.

## 4. Combining images

The program traverses the balloon's pixels and copies them onto the beach at an offset. It skips white pixels, blends transparency, and checks image boundaries.

Show `output/07-composite.png`.

## 5. Evaluation and ethics

All seven images were generated and their transformations checked. An image is naturally represented as rows and columns in a 2D array. Digital editing can be creative, but meaningful edits should be disclosed when viewers need an accurate record, such as in journalism or science.
