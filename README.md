# Content Aware Photo Editor

Hi there folks 👋

This project is my initiative on researching content aware image resizing. The repository contains a few
neat things:
- Implementation of Seam Carving algorithm: slow one working for `O(w*h*max(dw, dh))`, and a fast one working for `O(w*h)`. The fast algorithm is also applicable for runtime usage as it adds no additional complexity over image traversing. You can find the code at [lib-seamcarving module](https://github.com/st235/ContentAwarePhotoEditor/tree/main/lib-seamcarving);
- Wrappers around [Glide](https://github.com/bumptech/glide) and [Picasso](https://github.com/square/picasso). The wrappers can be found at: [lib-seacarving-glide](https://github.com/st235/ContentAwarePhotoEditor/tree/main/lib-seamcarving-glide) and [lib-seamcarving-picasso](https://github.com/st235/ContentAwarePhotoEditor/tree/main/lib-seamcarving-picasso) modules;
- [Sample app](https://github.com/st235/ContentAwarePhotoEditor/tree/main/sampleapp). Demonstrates how to use image libraries wrappers in realtime;
- [Editor](https://github.com/st235/ContentAwarePhotoEditor/tree/main/app). This is the main module of the whole repository. Demonstrates how to apply seam carving at image editing applications.

# Introduction

## Seam Carving

The main idea of seam carving is to provide a lossless way of retargeting images by removing the content from "not enough interesting" areas. You can see the example of the process below.

| Original image | Seams to remove | Result | 
| ------------- | ------------- | ------------- |
| <img src="/images/carving/origin.jpg" width="287" height="198">  | <img src="/images/carving/seams.jpeg" width="287" height="198">  | <img src="/images/carving/results.jpeg" width="198" height="198"> |

As you can see the image has been trimmed by removing content (highlighted in red) in-between important objects (_balloons_).

### Energy function

To build seams according to the limitations we use an heuristic function called an "energy" function. This function helps us to determine the important parts of the image.

For most of the operations [the Sobel operator](https://en.wikipedia.org/wiki/Sobel_operator) is more than enough to find right areas as it calculates a gradient vector's magnitude approximation. 
This approximation plays a big role in edge detection. [The implementation](https://github.com/st235/ContentAwarePhotoEditor/blob/main/lib-seamcarving/src/main/java/st235/com/github/seamcarving/energies/SobelEnergy.kt) 
works really good in our case as well. See the examples below:

| Original image | Energy | Notes |
| ------------- | ------------- | ------------- |
| <img src="/images/energy/origin_building2.jpeg" width="240" height="320">  | <img src="/images/energy/sobel_building2.jpeg" width="240" height="320">  | An example with clearly <br> distinguishable background |
| <img src="/images/energy/origin_building1.jpeg" width="240" height="320">  | <img src="/images/energy/sobel_building1.jpeg" width="240" height="320">  | Clouds make the background <br> less distinguishable than on example above, <br> but still keeps the object aside from background |
| <img src="/images/energy/origin_flower.jpeg" width="240" height="320">  | <img src="/images/energy/sobel_flower.jpeg" width="240" height="320">  | Hardly distinguishable background |

### Compexities concerns

| Criterion | Complexity |
| ------------- | ------------- |
| Runtime | O(w*h) |
| Memory | O(w*h) |

_So as we said before no "extra" complexity over image traversal. This is a really good complexity for software processing algorithms._

# Libraries

We picked the most famous libraries for Android and adapted our algorithm to them. These libraries are Glide and Picasso.

## Glide

We provide [SeamCarvingTransformation](https://github.com/st235/ContentAwarePhotoEditor/blob/main/lib-seamcarving-glide/src/main/java/st235/com/github/seamcarvingglide/SeamCarvingTransformation.kt) to use Seam Carving algorithm with Glide.

You can easily pull the library and integrate it into your codebase. The usage is pretty straightforward:

```kotlin
Glide.with(itemView.context)
    .load(imageRes)
    .apply(RequestOptions.bitmapTransform(SeamCarvingTransformation(sampling = 2)))
    .into(imageView)
```

## Picasso

Picasso structure is quite similar to the one in Glide, 
therefore we also provide [SeamCarvingTransformation](https://github.com/st235/ContentAwarePhotoEditor/blob/main/lib-seamcarving-picasso/src/main/java/st235/com/github/seamcarvingpicasso/SeamCarvingTransformation.kt) to adapt our algorithm to the library.

The example is given below:

```kotlin
Picasso.get()
    .load(imageRes)
    .transform(SeamCarvingTransformation(sampling = 2))
    .into(imageView)
```

## Examples

We also prepared a small demo library (you can find it in the sample app module). The main idea of the library is to show two things:
1. Prove that we can use the algorithm in runtime;
2. Compare the algorithm with one of existing approaches.

| Center Crop | Seams Carving |
| ------------- | ------------- |
| <img src="/images/sampleapp/default/balloons1.jpeg" width="296" height="427">  | <img src="/images/sampleapp/carving/balloons1.jpeg" width="296" height="427">  |
| <img src="/images/sampleapp/default/building1.jpeg" width="296" height="427">  | <img src="/images/sampleapp/carving/building1.jpeg" width="296" height="427">  |
| <img src="/images/sampleapp/default/lego1.jpeg" width="296" height="427">  | <img src="/images/sampleapp/carving/lego1.jpeg" width="296" height="427">  |
| <img src="/images/sampleapp/default/mountain1.jpeg" width="296" height="489">  | <img src="/images/sampleapp/carving/mountain1.jpeg" width="296" height="489">  |
| <img src="/images/sampleapp/default/tree1.jpeg" width="296" height="403">  | <img src="/images/sampleapp/carving/tree1.jpeg" width="296" height="403">  |

__P.S.: More examples available at the sample app!__

# Editor

The star of this repo is the editor (the app module). The editor supports manual handling of the seam carving with some advanced tooling onboard.
You can use brushes 🖌 to keep/remove objects from the image. Moreover, the editor also provides a mechanism for making accurate adjustments to the algorithm.  

<img src="/images/editor/gallery.jpeg" width="270" height="585">  <img src="/images/editor/editor.jpeg" width="270" height="585">

## Remove objects

The editor allows your to keep the same image size but remove some unwanted objects from it. Highlight the area that you wanna remove and then click apply. That is really easy!

| Original image | Editor | Result | 
| ------------- | ------------- | ------------- |
| <img src="/images/editor/removal/origin.jpeg" width="240" height="320">  | <img src="/images/editor/removal/editor_removal.jpeg" width="135" height="292">  | <img src="/images/editor/removal/result.jpeg" width="240" height="320"> |

## Keep objects

Moreover, the editor introduce a way to seek for guidance from the user and provide a way to them to highlight the objects that they want to keep if algorithm fails. 
For example, it can be really useful for objects that are hardly distinguishable from the background. The process is demonstrated below:

| Original image | Editor | Result | 
| ------------- | ------------- | ------------- |
| <img src="/images/editor/keeping/origin.jpeg" width="320" height="240">  | <img src="/images/editor/keeping/editor_keeping.jpeg" width="135" height="292">  | <img src="/images/editor/keeping/result.jpeg" width="240" height="240"> |

# Questions

Still have some questions? Contact me by opening [an issue](https://github.com/st235/ContentAwarePhotoEditor/issues). 

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
