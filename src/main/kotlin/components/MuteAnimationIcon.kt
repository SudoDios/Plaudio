package components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import org.jetbrains.skia.Rect
import org.jetbrains.skia.skottie.Animation
import org.jetbrains.skia.sksg.InvalidationController

@Composable
fun MuteAnimationIcon(
    modifier: Modifier,
    color: Color = Color.Red,
    isMute : Boolean
) {

    val source = """
{"nm":"MEDIA MUTE / UNMUTE","ddd":0,"h":512,"w":512,"meta":{"g":"@lottiefiles/toolkit-js 0.26.1"},"layers":[{"ty":4,"nm":"Line Small","sr":1,"st":0,"op":12,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[105.272,-4.983,0]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":1,"k":[{"o":{"x":0.6,"y":0},"i":{"x":0.6,"y":1},"s":[105.272,-4.983,0],"t":0,"ti":[-4.5,-0.5,0],"to":[4.5,0.5,0]},{"s":[132.272,-1.983,0],"t":13}]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}},"ef":[],"shapes":[{"ty":"gr","bm":0,"hd":false,"nm":"Group 1","it":[{"ty":"sh","bm":0,"hd":false,"nm":"Path 1","d":1,"ks":{"a":0,"k":{"c":false,"i":[[-15.933,-11.882],[0,-17.553],[17.385,-13.182]],"o":[[17.789,13.266],[0,17.323],[-15.643,11.861]],"v":[[91.693,-52.966],[118.852,-4.73],[92.365,43.001]]}}},{"ty":"tm","bm":0,"hd":false,"nm":"Trim Paths 1","e":{"a":1,"k":[{"o":{"x":0.6,"y":0},"i":{"x":0.6,"y":1},"s":[100],"t":0},{"s":[53],"t":13}]},"o":{"a":0,"k":0},"s":{"a":1,"k":[{"o":{"x":0.6,"y":0},"i":{"x":0.6,"y":1},"s":[0],"t":0},{"s":[47],"t":13}]},"m":1},{"ty":"st","bm":0,"hd":false,"nm":"Stroke 1","lc":2,"lj":2,"ml":1,"o":{"a":0,"k":100},"w":{"a":0,"k":30},"c":{"a":0,"k":[${color.red},${color.green},${color.blue}]}},{"ty":"tr","a":{"a":0,"k":[0,0]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":0,"k":[0,0]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}}]}],"ind":1,"parent":4},{"ty":4,"nm":"Line Big","sr":1,"st":0,"op":12,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[131.883,-5.898,0]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":1,"k":[{"o":{"x":0.6,"y":0},"i":{"x":0.6,"y":1},"s":[131.883,-5.898,0],"t":0,"ti":[0,0,0],"to":[0,0,0]},{"s":[100.883,0.102,0],"t":13}]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}},"ef":[],"shapes":[{"ty":"gr","bm":0,"hd":false,"nm":"Group 1","it":[{"ty":"sh","bm":0,"hd":false,"nm":"Path 1","d":1,"ks":{"a":0,"k":{"c":false,"i":[[-0.471,-0.164],[-15.933,-19.728],[0,-29.144],[17.385,-21.886],[25.381,-9.147]],"o":[[25.715,8.926],[17.789,22.026],[0,28.763],[-15.643,19.694],[-0.457,0.165]],"v":[[86.037,-128.22],[149.57,-84.036],[177.729,-5.608],[150.242,71.983],[87.678,116.424]]}}},{"ty":"st","bm":0,"hd":false,"nm":"Stroke 1","lc":2,"lj":2,"ml":1,"o":{"a":0,"k":100},"w":{"a":0,"k":30},"c":{"a":0,"k":[${color.red},${color.green},${color.blue}]}},{"ty":"tm","bm":0,"hd":false,"nm":"Trim Paths 1","e":{"a":1,"k":[{"o":{"x":0.6,"y":0},"i":{"x":0.6,"y":1},"s":[100],"t":0},{"s":[51.5],"t":13}]},"o":{"a":0,"k":0},"s":{"a":1,"k":[{"o":{"x":0.6,"y":0},"i":{"x":0.6,"y":1},"s":[0],"t":0},{"s":[48.5],"t":13}]},"m":1},{"ty":"tr","a":{"a":0,"k":[0,0]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":0,"k":[0,0]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}}]}],"ind":2,"parent":4},{"ty":4,"nm":"Cross","sr":1,"st":-1,"op":21,"ip":12,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[140.253,5.78,0]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":0,"k":[146.626,5.78,0]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}},"ef":[],"shapes":[{"ty":"gr","bm":0,"hd":false,"nm":"Group 1","it":[{"ty":"sh","bm":0,"hd":false,"nm":"Path 1","d":1,"ks":{"a":1,"k":[{"o":{"x":0.167,"y":0.167},"i":{"x":0.2,"y":1},"s":[{"c":false,"i":[[0,0],[0,0]],"o":[[0,0],[0,0]],"v":[[141.255,0.625],[139.252,10.936]]}],"t":12},{"s":[{"c":false,"i":[[0,0],[0,0]],"o":[[0,0],[0,0]],"v":[[186.424,-40.422],[94.083,51.983]]}],"t":21}]}},{"ty":"st","bm":0,"hd":false,"nm":"Stroke 1","lc":2,"lj":2,"ml":1,"o":{"a":0,"k":100},"w":{"a":0,"k":30},"c":{"a":0,"k":[${color.red},${color.green},${color.blue}]}},{"ty":"tr","a":{"a":0,"k":[139.75,5.5]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":0,"k":[139.75,-0.5]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}}]},{"ty":"gr","bm":0,"hd":false,"nm":"Group 2","it":[{"ty":"sh","bm":0,"hd":false,"nm":"Path 1","d":1,"ks":{"a":1,"k":[{"o":{"x":0.167,"y":0.167},"i":{"x":0.2,"y":1},"s":[{"c":false,"i":[[0,0],[0,0]],"o":[[0,0],[0,0]],"v":[[139.252,0.625],[141.255,10.936]]}],"t":12},{"s":[{"c":false,"i":[[0,0],[0,0]],"o":[[0,0],[0,0]],"v":[[94.083,-40.422],[186.424,51.983]]}],"t":21}]}},{"ty":"st","bm":0,"hd":false,"nm":"Stroke 1","lc":2,"lj":2,"ml":1,"o":{"a":0,"k":100},"w":{"a":0,"k":30},"c":{"a":0,"k":[${color.red},${color.green},${color.blue}]}},{"ty":"tr","a":{"a":0,"k":[139.75,5.5]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":0,"k":[139.75,-0.5]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}}]}],"ind":3,"parent":4},{"ty":4,"nm":"Speaker","sr":1,"st":0,"op":21,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[-71.881,0.078,0]},"s":{"a":1,"k":[{"o":{"x":0.6,"y":0},"i":{"x":0.7,"y":1},"s":[100,100,100],"t":0},{"o":{"x":0.3,"y":0},"i":{"x":0.3,"y":1},"s":[93,93,100],"t":12},{"s":[100,100,100],"t":21}]},"sk":{"a":0,"k":0},"p":{"a":1,"k":[{"o":{"x":0.6,"y":0},"i":{"x":0.2,"y":1},"s":[184.119,256.078,0],"t":0,"ti":[0,0,0],"to":[-1.062,0,0]},{"s":[177.746,256.078,0],"t":21}]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}},"ef":[],"shapes":[{"ty":"gr","bm":0,"hd":false,"nm":"Group 1","it":[{"ty":"sh","bm":0,"hd":false,"nm":"Path 1","d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,0],[16.806,-17.492],[0,0],[6.343,0],[0,0],[0,-14.237],[0,0],[-22.972,0],[0,0],[-4.378,-4.611],[0,0],[0,37.153],[0,0]],"o":[[0,-37.153],[0,0],[-4.378,4.611],[0,0],[-22.972,0],[0,0],[0,14.237],[0,0],[6.343,0],[0,0],[16.806,17.492],[0,0],[0,0]],"v":[[33.271,-121.847],[-15.728,-144.085],[-80.954,-75.396],[-97.742,-68.176],[-144.423,-68.176],[-177.034,-39.797],[-177.034,39.953],[-144.423,68.333],[-97.742,68.333],[-80.954,75.552],[-15.728,144.241],[33.271,122.004],[33.271,2.96]]}}},{"ty":"st","bm":0,"hd":false,"nm":"Stroke 1","lc":2,"lj":2,"ml":1,"o":{"a":0,"k":100},"w":{"a":0,"k":30},"c":{"a":0,"k":[${color.red},${color.green},${color.blue}]}},{"ty":"tr","a":{"a":0,"k":[0,0]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":0,"k":[0,0]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}}]}],"ind":4},{"ty":4,"nm":"Line Small 2","sr":1,"st":33,"op":179,"ip":31,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[105.272,-4.983,0]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":1,"k":[{"o":{"x":0.1,"y":0},"i":{"x":0.2,"y":1},"s":[20.883,0.352,0],"t":31,"ti":[0,0,0],"to":[0,0,0]},{"s":[105.272,-4.983,0],"t":43}]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}},"ef":[],"shapes":[{"ty":"gr","bm":0,"hd":false,"nm":"Group 1","it":[{"ty":"sh","bm":0,"hd":false,"nm":"Path 1","d":1,"ks":{"a":0,"k":{"c":false,"i":[[-15.933,-11.882],[0,-17.553],[17.385,-13.182]],"o":[[17.789,13.266],[0,17.323],[-15.643,11.861]],"v":[[91.693,-52.966],[118.852,-4.73],[92.365,43.001]]}}},{"ty":"tm","bm":0,"hd":false,"nm":"Trim Paths 1","e":{"a":1,"k":[{"o":{"x":0.1,"y":0},"i":{"x":0.2,"y":1},"s":[51.5],"t":31},{"s":[100],"t":43}]},"o":{"a":0,"k":0},"s":{"a":1,"k":[{"o":{"x":0.1,"y":0},"i":{"x":0.2,"y":1},"s":[48.5],"t":31},{"s":[0],"t":43}]},"m":1},{"ty":"st","bm":0,"hd":false,"nm":"Stroke 1","lc":2,"lj":2,"ml":1,"o":{"a":0,"k":100},"w":{"a":0,"k":30},"c":{"a":0,"k":[${color.red},${color.green},${color.blue}]}},{"ty":"tr","a":{"a":0,"k":[0,0]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":0,"k":[0,0]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}}]}],"ind":5,"parent":8},{"ty":4,"nm":"Line Big 2","sr":1,"st":33,"op":179,"ip":31,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[131.883,-5.898,0]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":1,"k":[{"o":{"x":0.1,"y":0},"i":{"x":0.2,"y":1},"s":[80.133,0.102,0],"t":31,"ti":[0,0,0],"to":[0,0,0]},{"s":[131.883,-5.898,0],"t":43}]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}},"ef":[],"shapes":[{"ty":"gr","bm":0,"hd":false,"nm":"Group 1","it":[{"ty":"sh","bm":0,"hd":false,"nm":"Path 1","d":1,"ks":{"a":0,"k":{"c":false,"i":[[-0.471,-0.164],[-15.933,-19.728],[0,-29.144],[17.385,-21.886],[25.381,-9.147]],"o":[[25.715,8.926],[17.789,22.026],[0,28.763],[-15.643,19.694],[-0.457,0.165]],"v":[[86.037,-128.22],[149.57,-84.036],[177.729,-5.608],[150.242,71.983],[87.678,116.424]]}}},{"ty":"st","bm":0,"hd":false,"nm":"Stroke 1","lc":2,"lj":2,"ml":1,"o":{"a":0,"k":100},"w":{"a":0,"k":30},"c":{"a":0,"k":[${color.red},${color.green},${color.blue}]}},{"ty":"tm","bm":0,"hd":false,"nm":"Trim Paths 1","e":{"a":1,"k":[{"o":{"x":0.1,"y":0},"i":{"x":0.2,"y":1},"s":[60],"t":31},{"s":[100],"t":43}]},"o":{"a":0,"k":0},"s":{"a":1,"k":[{"o":{"x":0.1,"y":0},"i":{"x":0.2,"y":1},"s":[40],"t":31},{"s":[0],"t":43}]},"m":1},{"ty":"tr","a":{"a":0,"k":[0,0]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":0,"k":[0,0]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}}]}],"ind":6,"parent":8},{"ty":4,"nm":"Cross 2","sr":1,"st":20,"op":31,"ip":21,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[140.253,5.78,0]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":1,"k":[{"o":{"x":0.6,"y":0},"i":{"x":0.6,"y":1},"s":[146.626,5.78,0],"t":21,"ti":[3,0,0],"to":[-3,0,0]},{"s":[128.626,5.78,0],"t":32}]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}},"ef":[],"shapes":[{"ty":"gr","bm":0,"hd":false,"nm":"Group 1","it":[{"ty":"sh","bm":0,"hd":false,"nm":"Path 1","d":1,"ks":{"a":1,"k":[{"o":{"x":0.6,"y":0},"i":{"x":0.6,"y":1},"s":[{"c":false,"i":[[0,0],[0,0]],"o":[[0,0],[0,0]],"v":[[186.424,-40.422],[94.083,51.983]]}],"t":21},{"s":[{"c":false,"i":[[0,0],[0,0]],"o":[[0,0],[0,0]],"v":[[141.255,0.625],[139.252,10.936]]}],"t":32}]}},{"ty":"st","bm":0,"hd":false,"nm":"Stroke 1","lc":2,"lj":2,"ml":1,"o":{"a":0,"k":100},"w":{"a":0,"k":30},"c":{"a":0,"k":[${color.red},${color.green},${color.blue}]}},{"ty":"tr","a":{"a":0,"k":[139.75,5.5]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":0,"k":[139.75,-0.5]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}}]},{"ty":"gr","bm":0,"hd":false,"nm":"Group 2","it":[{"ty":"sh","bm":0,"hd":false,"nm":"Path 1","d":1,"ks":{"a":1,"k":[{"o":{"x":0.6,"y":0},"i":{"x":0.6,"y":1},"s":[{"c":false,"i":[[0,0],[0,0]],"o":[[0,0],[0,0]],"v":[[94.083,-40.422],[186.424,51.983]]}],"t":21},{"s":[{"c":false,"i":[[0,0],[0,0]],"o":[[0,0],[0,0]],"v":[[139.252,0.625],[141.255,10.936]]}],"t":32}]}},{"ty":"st","bm":0,"hd":false,"nm":"Stroke 1","lc":2,"lj":2,"ml":1,"o":{"a":0,"k":100},"w":{"a":0,"k":30},"c":{"a":0,"k":[${color.red},${color.green},${color.blue}]}},{"ty":"tr","a":{"a":0,"k":[139.75,5.5]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":0,"k":[139.75,-0.5]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}}]}],"ind":7,"parent":8},{"ty":4,"nm":"Speaker 2","sr":1,"st":21,"op":184,"ip":21,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[-71.881,0.078,0]},"s":{"a":1,"k":[{"o":{"x":0.6,"y":0},"i":{"x":0.7,"y":1},"s":[100,100,100],"t":21},{"o":{"x":0.3,"y":0},"i":{"x":0.3,"y":1},"s":[93,93,100],"t":30},{"s":[100,100,100],"t":43}]},"sk":{"a":0,"k":0},"p":{"a":1,"k":[{"o":{"x":0.6,"y":0},"i":{"x":0.2,"y":1},"s":[177.746,256.078,0],"t":21,"ti":[-1.062,0,0],"to":[0,0,0]},{"s":[184.119,256.078,0],"t":43}]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}},"ef":[],"shapes":[{"ty":"gr","bm":0,"hd":false,"nm":"Group 1","it":[{"ty":"sh","bm":0,"hd":false,"nm":"Path 1","d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,0],[16.806,-17.492],[0,0],[6.343,0],[0,0],[0,-14.237],[0,0],[-22.972,0],[0,0],[-4.378,-4.611],[0,0],[0,37.153],[0,0]],"o":[[0,-37.153],[0,0],[-4.378,4.611],[0,0],[-22.972,0],[0,0],[0,14.237],[0,0],[6.343,0],[0,0],[16.806,17.492],[0,0],[0,0]],"v":[[33.271,-121.847],[-15.728,-144.085],[-80.954,-75.396],[-97.742,-68.176],[-144.423,-68.176],[-177.034,-39.797],[-177.034,39.953],[-144.423,68.333],[-97.742,68.333],[-80.954,75.552],[-15.728,144.241],[33.271,122.004],[33.271,2.96]]}}},{"ty":"st","bm":0,"hd":false,"nm":"Stroke 1","lc":2,"lj":2,"ml":1,"o":{"a":0,"k":100},"w":{"a":0,"k":30},"c":{"a":0,"k":[${color.red},${color.green},${color.blue}]}},{"ty":"tr","a":{"a":0,"k":[0,0]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":0,"k":[0,0]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}}]}],"ind":8}],"v":"5.5.2","fr":60,"op":44,"ip":0,"assets":[{"nm":"","id":"comp_0","layers":[{"ty":4,"nm":"Line Small 4","sr":1,"st":0,"op":12,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[105.272,-4.983,0]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":1,"k":[{"o":{"x":0.6,"y":0},"i":{"x":0.6,"y":1},"s":[105.272,-4.983,0],"t":0,"ti":[-4.5,-0.5,0],"to":[4.5,0.5,0]},{"s":[132.272,-1.983,0],"t":13}]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}},"ef":[],"shapes":[{"ty":"gr","bm":0,"hd":false,"nm":"Group 1","it":[{"ty":"sh","bm":0,"hd":false,"nm":"Path 1","d":1,"ks":{"a":0,"k":{"c":false,"i":[[-15.933,-11.882],[0,-17.553],[17.385,-13.182]],"o":[[17.789,13.266],[0,17.323],[-15.643,11.861]],"v":[[91.693,-52.966],[118.852,-4.73],[92.365,43.001]]}}},{"ty":"tm","bm":0,"hd":false,"nm":"Trim Paths 1","e":{"a":1,"k":[{"o":{"x":0.6,"y":0},"i":{"x":0.6,"y":1},"s":[100],"t":0},{"s":[53],"t":13}]},"o":{"a":0,"k":0},"s":{"a":1,"k":[{"o":{"x":0.6,"y":0},"i":{"x":0.6,"y":1},"s":[0],"t":0},{"s":[47],"t":13}]},"m":1},{"ty":"st","bm":0,"hd":false,"nm":"Stroke 1","lc":2,"lj":2,"ml":1,"o":{"a":0,"k":100},"w":{"a":0,"k":46},"c":{"a":0,"k":[0,0,0]}},{"ty":"tr","a":{"a":0,"k":[0,0]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":0,"k":[0,0]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}}]}],"ind":1,"parent":4},{"ty":4,"nm":"Line Big 4","sr":1,"st":0,"op":12,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[131.883,-5.898,0]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":1,"k":[{"o":{"x":0.6,"y":0},"i":{"x":0.6,"y":1},"s":[131.883,-5.898,0],"t":0,"ti":[0,0,0],"to":[0,0,0]},{"s":[100.883,0.102,0],"t":13}]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}},"ef":[],"shapes":[{"ty":"gr","bm":0,"hd":false,"nm":"Group 1","it":[{"ty":"sh","bm":0,"hd":false,"nm":"Path 1","d":1,"ks":{"a":0,"k":{"c":false,"i":[[-0.471,-0.164],[-15.933,-19.728],[0,-29.144],[17.385,-21.886],[25.381,-9.147]],"o":[[25.715,8.926],[17.789,22.026],[0,28.763],[-15.643,19.694],[-0.457,0.165]],"v":[[86.037,-128.22],[149.57,-84.036],[177.729,-5.608],[150.242,71.983],[87.678,116.424]]}}},{"ty":"st","bm":0,"hd":false,"nm":"Stroke 1","lc":2,"lj":2,"ml":1,"o":{"a":0,"k":100},"w":{"a":0,"k":46},"c":{"a":0,"k":[0,0,0]}},{"ty":"tm","bm":0,"hd":false,"nm":"Trim Paths 1","e":{"a":1,"k":[{"o":{"x":0.6,"y":0},"i":{"x":0.6,"y":1},"s":[100],"t":0},{"s":[51.5],"t":13}]},"o":{"a":0,"k":0},"s":{"a":1,"k":[{"o":{"x":0.6,"y":0},"i":{"x":0.6,"y":1},"s":[0],"t":0},{"s":[48.5],"t":13}]},"m":1},{"ty":"tr","a":{"a":0,"k":[0,0]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":0,"k":[0,0]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}}]}],"ind":2,"parent":4},{"ty":4,"nm":"Cross 4","sr":1,"st":-1,"op":21,"ip":12,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[140.253,5.78,0]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":0,"k":[146.626,5.78,0]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}},"ef":[],"shapes":[{"ty":"gr","bm":0,"hd":false,"nm":"Group 1","it":[{"ty":"sh","bm":0,"hd":false,"nm":"Path 1","d":1,"ks":{"a":1,"k":[{"o":{"x":0.167,"y":0.167},"i":{"x":0.2,"y":1},"s":[{"c":false,"i":[[0,0],[0,0]],"o":[[0,0],[0,0]],"v":[[141.255,0.625],[139.252,10.936]]}],"t":12},{"s":[{"c":false,"i":[[0,0],[0,0]],"o":[[0,0],[0,0]],"v":[[186.424,-40.422],[94.083,51.983]]}],"t":21}]}},{"ty":"st","bm":0,"hd":false,"nm":"Stroke 1","lc":2,"lj":2,"ml":1,"o":{"a":0,"k":100},"w":{"a":0,"k":46},"c":{"a":0,"k":[0,0,0]}},{"ty":"tr","a":{"a":0,"k":[139.75,5.5]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":0,"k":[139.75,-0.5]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}}]},{"ty":"gr","bm":0,"hd":false,"nm":"Group 2","it":[{"ty":"sh","bm":0,"hd":false,"nm":"Path 1","d":1,"ks":{"a":1,"k":[{"o":{"x":0.167,"y":0.167},"i":{"x":0.2,"y":1},"s":[{"c":false,"i":[[0,0],[0,0]],"o":[[0,0],[0,0]],"v":[[139.252,0.625],[141.255,10.936]]}],"t":12},{"s":[{"c":false,"i":[[0,0],[0,0]],"o":[[0,0],[0,0]],"v":[[94.083,-40.422],[186.424,51.983]]}],"t":21}]}},{"ty":"st","bm":0,"hd":false,"nm":"Stroke 1","lc":2,"lj":2,"ml":1,"o":{"a":0,"k":100},"w":{"a":0,"k":46},"c":{"a":0,"k":[0,0,0]}},{"ty":"tr","a":{"a":0,"k":[139.75,5.5]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":0,"k":[139.75,-0.5]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}}]}],"ind":3,"parent":4},{"ty":4,"nm":"Speaker 4","sr":1,"st":0,"op":21,"ip":0,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[-71.881,0.078,0]},"s":{"a":1,"k":[{"o":{"x":0.6,"y":0},"i":{"x":0.7,"y":1},"s":[100,100,100],"t":0},{"o":{"x":0.3,"y":0},"i":{"x":0.3,"y":1},"s":[93,93,100],"t":12},{"s":[100,100,100],"t":21}]},"sk":{"a":0,"k":0},"p":{"a":1,"k":[{"o":{"x":0.6,"y":0},"i":{"x":0.2,"y":1},"s":[184.119,256.078,0],"t":0,"ti":[0,0,0],"to":[-1.062,0,0]},{"s":[177.746,256.078,0],"t":21}]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}},"ef":[],"shapes":[{"ty":"gr","bm":0,"hd":false,"nm":"Group 1","it":[{"ty":"sh","bm":0,"hd":false,"nm":"Path 1","d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,0],[16.806,-17.492],[0,0],[6.343,0],[0,0],[0,-14.237],[0,0],[-22.972,0],[0,0],[-4.378,-4.611],[0,0],[0,37.153],[0,0]],"o":[[0,-37.153],[0,0],[-4.378,4.611],[0,0],[-22.972,0],[0,0],[0,14.237],[0,0],[6.343,0],[0,0],[16.806,17.492],[0,0],[0,0]],"v":[[33.271,-121.847],[-15.728,-144.085],[-80.954,-75.396],[-97.742,-68.176],[-144.423,-68.176],[-177.034,-39.797],[-177.034,39.953],[-144.423,68.333],[-97.742,68.333],[-80.954,75.552],[-15.728,144.241],[33.271,122.004],[33.271,2.96]]}}},{"ty":"st","bm":0,"hd":false,"nm":"Stroke 1","lc":2,"lj":2,"ml":1,"o":{"a":0,"k":100},"w":{"a":0,"k":46},"c":{"a":0,"k":[0,0,0]}},{"ty":"tr","a":{"a":0,"k":[0,0]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":0,"k":[0,0]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}}]}],"ind":4},{"ty":4,"nm":"Line Small 3","sr":1,"st":33,"op":179,"ip":31,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[105.272,-4.983,0]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":1,"k":[{"o":{"x":0.1,"y":0},"i":{"x":0.2,"y":1},"s":[20.883,0.352,0],"t":31,"ti":[0,0,0],"to":[0,0,0]},{"s":[105.272,-4.983,0],"t":43}]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}},"ef":[],"shapes":[{"ty":"gr","bm":0,"hd":false,"nm":"Group 1","it":[{"ty":"sh","bm":0,"hd":false,"nm":"Path 1","d":1,"ks":{"a":0,"k":{"c":false,"i":[[-15.933,-11.882],[0,-17.553],[17.385,-13.182]],"o":[[17.789,13.266],[0,17.323],[-15.643,11.861]],"v":[[91.693,-52.966],[118.852,-4.73],[92.365,43.001]]}}},{"ty":"tm","bm":0,"hd":false,"nm":"Trim Paths 1","e":{"a":1,"k":[{"o":{"x":0.1,"y":0},"i":{"x":0.2,"y":1},"s":[51.5],"t":31},{"s":[100],"t":43}]},"o":{"a":0,"k":0},"s":{"a":1,"k":[{"o":{"x":0.1,"y":0},"i":{"x":0.2,"y":1},"s":[48.5],"t":31},{"s":[0],"t":43}]},"m":1},{"ty":"st","bm":0,"hd":false,"nm":"Stroke 1","lc":2,"lj":2,"ml":1,"o":{"a":0,"k":100},"w":{"a":0,"k":46},"c":{"a":0,"k":[0,0,0]}},{"ty":"tr","a":{"a":0,"k":[0,0]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":0,"k":[0,0]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}}]}],"ind":5,"parent":8},{"ty":4,"nm":"Line Big 3","sr":1,"st":33,"op":179,"ip":31,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[131.883,-5.898,0]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":1,"k":[{"o":{"x":0.1,"y":0},"i":{"x":0.2,"y":1},"s":[80.133,0.102,0],"t":31,"ti":[0,0,0],"to":[0,0,0]},{"s":[131.883,-5.898,0],"t":43}]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}},"ef":[],"shapes":[{"ty":"gr","bm":0,"hd":false,"nm":"Group 1","it":[{"ty":"sh","bm":0,"hd":false,"nm":"Path 1","d":1,"ks":{"a":0,"k":{"c":false,"i":[[-0.471,-0.164],[-15.933,-19.728],[0,-29.144],[17.385,-21.886],[25.381,-9.147]],"o":[[25.715,8.926],[17.789,22.026],[0,28.763],[-15.643,19.694],[-0.457,0.165]],"v":[[86.037,-128.22],[149.57,-84.036],[177.729,-5.608],[150.242,71.983],[87.678,116.424]]}}},{"ty":"st","bm":0,"hd":false,"nm":"Stroke 1","lc":2,"lj":2,"ml":1,"o":{"a":0,"k":100},"w":{"a":0,"k":46},"c":{"a":0,"k":[0,0,0]}},{"ty":"tm","bm":0,"hd":false,"nm":"Trim Paths 1","e":{"a":1,"k":[{"o":{"x":0.1,"y":0},"i":{"x":0.2,"y":1},"s":[60],"t":31},{"s":[100],"t":43}]},"o":{"a":0,"k":0},"s":{"a":1,"k":[{"o":{"x":0.1,"y":0},"i":{"x":0.2,"y":1},"s":[40],"t":31},{"s":[0],"t":43}]},"m":1},{"ty":"tr","a":{"a":0,"k":[0,0]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":0,"k":[0,0]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}}]}],"ind":6,"parent":8},{"ty":4,"nm":"Cross 3","sr":1,"st":20,"op":31,"ip":21,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[140.253,5.78,0]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":1,"k":[{"o":{"x":0.6,"y":0},"i":{"x":0.6,"y":1},"s":[146.626,5.78,0],"t":21,"ti":[3,0,0],"to":[-3,0,0]},{"s":[128.626,5.78,0],"t":32}]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}},"ef":[],"shapes":[{"ty":"gr","bm":0,"hd":false,"nm":"Group 1","it":[{"ty":"sh","bm":0,"hd":false,"nm":"Path 1","d":1,"ks":{"a":1,"k":[{"o":{"x":0.6,"y":0},"i":{"x":0.6,"y":1},"s":[{"c":false,"i":[[0,0],[0,0]],"o":[[0,0],[0,0]],"v":[[186.424,-40.422],[94.083,51.983]]}],"t":21},{"s":[{"c":false,"i":[[0,0],[0,0]],"o":[[0,0],[0,0]],"v":[[141.255,0.625],[139.252,10.936]]}],"t":32}]}},{"ty":"st","bm":0,"hd":false,"nm":"Stroke 1","lc":2,"lj":2,"ml":1,"o":{"a":0,"k":100},"w":{"a":0,"k":46},"c":{"a":0,"k":[0,0,0]}},{"ty":"tr","a":{"a":0,"k":[139.75,5.5]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":0,"k":[139.75,-0.5]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}}]},{"ty":"gr","bm":0,"hd":false,"nm":"Group 2","it":[{"ty":"sh","bm":0,"hd":false,"nm":"Path 1","d":1,"ks":{"a":1,"k":[{"o":{"x":0.6,"y":0},"i":{"x":0.6,"y":1},"s":[{"c":false,"i":[[0,0],[0,0]],"o":[[0,0],[0,0]],"v":[[94.083,-40.422],[186.424,51.983]]}],"t":21},{"s":[{"c":false,"i":[[0,0],[0,0]],"o":[[0,0],[0,0]],"v":[[139.252,0.625],[141.255,10.936]]}],"t":32}]}},{"ty":"st","bm":0,"hd":false,"nm":"Stroke 1","lc":2,"lj":2,"ml":1,"o":{"a":0,"k":100},"w":{"a":0,"k":46},"c":{"a":0,"k":[0,0,0]}},{"ty":"tr","a":{"a":0,"k":[139.75,5.5]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":0,"k":[139.75,-0.5]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}}]}],"ind":7,"parent":8},{"ty":4,"nm":"Speaker 3","sr":1,"st":21,"op":184,"ip":21,"hd":false,"ddd":0,"bm":0,"hasMask":false,"ao":0,"ks":{"a":{"a":0,"k":[-71.881,0.078,0]},"s":{"a":1,"k":[{"o":{"x":0.6,"y":0},"i":{"x":0.7,"y":1},"s":[100,100,100],"t":21},{"o":{"x":0.3,"y":0},"i":{"x":0.3,"y":1},"s":[93,93,100],"t":30},{"s":[100,100,100],"t":43}]},"sk":{"a":0,"k":0},"p":{"a":1,"k":[{"o":{"x":0.6,"y":0},"i":{"x":0.2,"y":1},"s":[177.746,256.078,0],"t":21,"ti":[-1.062,0,0],"to":[0,0,0]},{"s":[184.119,256.078,0],"t":43}]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}},"ef":[],"shapes":[{"ty":"gr","bm":0,"hd":false,"nm":"Group 1","it":[{"ty":"sh","bm":0,"hd":false,"nm":"Path 1","d":1,"ks":{"a":0,"k":{"c":true,"i":[[0,0],[16.806,-17.492],[0,0],[6.343,0],[0,0],[0,-14.237],[0,0],[-22.972,0],[0,0],[-4.378,-4.611],[0,0],[0,37.153],[0,0]],"o":[[0,-37.153],[0,0],[-4.378,4.611],[0,0],[-22.972,0],[0,0],[0,14.237],[0,0],[6.343,0],[0,0],[16.806,17.492],[0,0],[0,0]],"v":[[33.271,-121.847],[-15.728,-144.085],[-80.954,-75.396],[-97.742,-68.176],[-144.423,-68.176],[-177.034,-39.797],[-177.034,39.953],[-144.423,68.333],[-97.742,68.333],[-80.954,75.552],[-15.728,144.241],[33.271,122.004],[33.271,2.96]]}}},{"ty":"st","bm":0,"hd":false,"nm":"Stroke 1","lc":2,"lj":2,"ml":1,"o":{"a":0,"k":100},"w":{"a":0,"k":46},"c":{"a":0,"k":[0,0,0]}},{"ty":"tr","a":{"a":0,"k":[0,0]},"s":{"a":0,"k":[100,100]},"sk":{"a":0,"k":0},"p":{"a":0,"k":[0,0]},"r":{"a":0,"k":0},"sa":{"a":0,"k":0},"o":{"a":0,"k":100}}]}],"ind":8}]}]}
"""

    val animation = Animation.makeFromString(source)

    val animate = animateFloatAsState(
        targetValue = if (isMute) (animation.duration / 2f) else 0f,
        animationSpec = tween(550)
    )

    val invalidationController = remember { InvalidationController() }
    animation.seekFrameTime(animate.value, invalidationController)

    Canvas(modifier) {
        drawIntoCanvas {
            animation.render(
                canvas = it.nativeCanvas,
                dst = Rect.makeWH(size.width, size.height)
            )
        }
    }

}