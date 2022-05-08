# Content Aware Photo Editor

Hi there folks 👋

This project is my initiative on researching content aware image resizing. The repository contains a few
neat things:
- Implementation of Seam Carving algorithm: slow one working for `O(w*h*max(dw, dh))`, and a fast one working for `O(w*h)`. The last one is applicable for runtime usage. You can find the code at [lib-seamcarving module](https://github.com/st235/ContentAwarePhotoEditor/tree/main/lib-seamcarving);
- Wrappers around [Glide](https://github.com/bumptech/glide) and [Picasso](https://github.com/square/picasso). The wrappers can be found at: [lib-seacarving-glide](https://github.com/st235/ContentAwarePhotoEditor/tree/main/lib-seamcarving-glide) and [lib-seamcarving-picasso](https://github.com/st235/ContentAwarePhotoEditor/tree/main/lib-seamcarving-picasso) modules;
- [Sample app](https://github.com/st235/ContentAwarePhotoEditor/tree/main/sampleapp). Demonstrates how to use image libraries wrappers in realtime;
- [Editor](https://github.com/st235/ContentAwarePhotoEditor/tree/main/app). This is the main module of the whole repository. Demonstrates how to apply seam carving at image editing applications.

# Introduction

## Seam Carving

The main idea of seam carving is to provide lossless way of retargeting images by removing the content from "non enough interesting" spaces. You can see the example below.

| Original image | Seams to remove | Result | 
| ------------- | ------------- | ------------- |
| <img src="/images/carving/origin.jpg" width="574" height="396">  | <img src="/images/carving/seams.jpeg" width="574" height="396">  | <img src="/images/carving/results.jpeg" width="350" height="350"> |

As you can see the image has been trimmed by removing content in-between important objects.

### Energy function

To build seams right we use heuristic function called "energy" function. This function helps us to determine the important part of the image.

We are using [Sobel operator](https://en.wikipedia.org/wiki/Sobel_operator) to find a gradient vector's magnitude approximation. [The implementation](https://github.com/st235/ContentAwarePhotoEditor/blob/main/lib-seamcarving/src/main/java/st235/com/github/seamcarving/energies/SobelEnergy.kt) works really well, you can see the examples below:

| Original image | Energy | Notes |
| ------------- | ------------- | ------------- |
| <img src="/images/energy/origin_building2.jpeg" width="240" height="320">  | <img src="/images/energy/sobel_building2.jpeg" width="240" height="320">  | An example with clearly distinguishable background |
| <img src="/images/energy/origin_building1.jpeg" width="240" height="320">  | <img src="/images/energy/sobel_building1.jpeg" width="240" height="320">  | Clouds make the background less distinguishable than on example above, but still keeps the object aside from background |
| <img src="/images/energy/origin_flower.jpeg" width="240" height="320">  | <img src="/images/energy/sobel_flower.jpeg" width="240" height="320">  | Hardly distinguishable background |

### Compexities concerns

| Criterion | Complexity |
| ------------- | ------------- |
| Runtime | O(w*h) |
| Memory | O(w*h) |

So, no "extra" complexity over image traversal. This is a really good complexity for software processing algorithms.

# Libraries

The library is adapted for Glide and Picasso.

## Glide

We provide [SeamCarvingTransformation](https://github.com/st235/ContentAwarePhotoEditor/blob/main/lib-seamcarving-glide/src/main/java/st235/com/github/seamcarvingglide/SeamCarvingTransformation.kt) to use Seam Carving algorithm with Glide.

You can easily pull the library to your code and start using it. The usage example can be found in sample app although we still give you an example:

```kotlin
Glide.with(itemView.context)
    .load(imageRes)
    .apply(RequestOptions.bitmapTransform(SeamCarvingTransformation(sampling = 2)))
    .into(imageView)
```

## Picasso

The code for Picasso is quite similar. We also provide [SeamCarvingTransformation](https://github.com/st235/ContentAwarePhotoEditor/blob/main/lib-seamcarving-picasso/src/main/java/st235/com/github/seamcarvingpicasso/SeamCarvingTransformation.kt). See the example below:

```kotlin
Picasso.get()
    .load(imageRes)
    .transform(SeamCarvingTransformation(sampling = 2))
    .into(imageView)
```

## Examples

| Center Crop | Seams Carving |
| ------------- | ------------- |
| <img src="/images/sampleapp/default/balloons1.jpeg" width="591" height="853">  | <img src="/images/sampleapp/carving/balloons1.jpeg" width="591" height="853">  |
| <img src="/images/sampleapp/default/building1.jpeg" width="591" height="853">  | <img src="/images/sampleapp/carving/building1.jpeg" width="591" height="853">  |
| <img src="/images/sampleapp/default/lego1.jpeg" width="591" height="853">  | <img src="/images/sampleapp/carving/lego1.jpeg" width="591" height="853">  |
| <img src="/images/sampleapp/default/mountain1.jpeg" width="591" height="978">  | <img src="/images/sampleapp/carving/mountain1.jpeg" width="591" height="978">  |
| <img src="/images/sampleapp/default/tree1.jpeg" width="591" height="806">  | <img src="/images/sampleapp/carving/tree1.jpeg" width="591" height="806">  |

__P.S.: More examples available at sample app!__

# Editor

Editor supports manual handling of the seam carving. 

<img src="/images/editor/gallery.jpeg" width="270" height="585">  <img src="/images/editor/editor.jpeg" width="270" height="585">

## Remove object

The editor allows your to keep the same image size but remove some unwanted objects from it. Use the removal brush to select the objects and then apply the algorithm to the image.

| Original image | Editor | Result | 
| ------------- | ------------- | ------------- |
| <img src="/images/editor/removal/origin.jpeg" width="480" height="640">  | <img src="/images/editor/removal/editor_removal.jpeg" width="270" height="585">  | <img src="/images/editor/removal/result.jpeg" width="480" height="640"> |

## Keep object

Moreover, the editor allows your to keep some objects that are hardly distinguishable from the background. You can manually select them using keeping brush.

| Original image | Editor | Result | 
| ------------- | ------------- | ------------- |
| <img src="/images/editor/keeping/origin.jpeg" width="640" height="480">  | <img src="/images/editor/keeping/editor_keeping.jpeg" width="270" height="585">  | <img src="/images/editor/keeping/result.jpeg" width="480" height="480"> |

# Questions

Still have any questions? Contact me by opening [an issue](https://github.com/st235/ContentAwarePhotoEditor/issues) in the project!

### License

```
MIT License

Copyright (c) 2022 Alexander Dadukin

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
