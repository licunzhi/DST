<template>
  <div>
    <el-row :span="24">
     <el-col :span="10">
       <canvas id="canvas"></canvas>
       <canvas style="display: none" id="sCanvas"></canvas>
     </el-col>
    </el-row>
    <el-row :span="24" >
        <pre>
            <code>
            _____       _____     _______
            |  __ \     / ____|   |__   __|
            | |  | |   | (___        | |
            | |  | |    \___ \       | |
            | |__| |    ____) |      | |
            |_____/    |_____/       |_|

            Data Service Tools

            Date: 2021 - now
            Author: mm zz
            github:  mm -> https://github.com/836537471
            zz -> https://github.com/licunzhi
            ---------------------------------
            gitee:   mm -> https://gitee.com/caiminzs
            zz -> https://gitee.com/mk567

          </code>
        </pre>
    </el-row>
  </div>
</template>

<script>
let words = ['DST']
class Circle {
  constructor (x, y, ctx) {
    this.x = x
    this.y = y
    this.r = 1
    this.ctx = ctx
    this.isGrowing = true
  }
  growing () {
    if (this.isGrowing) {
      this.r++
    }
  }
  show () {
    this.ctx.beginPath()
    this.ctx.arc(this.x, this.y, this.r, 0, 2 * Math.PI)
    this.ctx.stroke()
    this.ctx.closePath()
  }
}

export default {
  name: 'dst',
  methods: {
    dist (x1, y1, x2, y2) {
      return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2))
    },
    gRA (min, max) {
      min = Math.ceil(min)
      max = Math.floor(max)
      return Math.floor(Math.random() * (max - min + 1)) + min
    },
    newCircle (possibleSpots, circles, ctx) {
      let newSpot = possibleSpots[this.gRA(0, possibleSpots.length - 1)]
      let draw = true
      for (let i = 0; i < circles.length; i++) {
        let circle2 = circles[i]
        if (this.dist(circle2.x, circle2.y, newSpot.x, newSpot.y) <= circle2.r) {
          draw = false
          break
        }
      }
      if (draw) {
        circles.push(new Circle(newSpot.x, newSpot.y, ctx))
      }
    },
    loop (ctx, counter, quality, circles, w, h, possibleSpots) {
      ctx.clearRect(0, 0, w, h)
      counter++

      for (let i = 0; i < quality; i++) {
        this.newCircle(possibleSpots, circles, ctx)
      }

      for (let circle of circles) {
        for (let circle1 of circles) {
          if (circle !== circle1) {
            if (this.dist(circle.x, circle.y, circle1.x, circle1.y) < circle.r + circle1.r) { // checking intersection
              circle.isGrowing = false
              break
            }
          }
        }
        circle.show()
        circle.growing()
      }

      const that = this

      window.requestAnimationFrame(function () { that.loop(ctx, counter, quality, circles, w, h, possibleSpots) })
    },
    initCanvas () {
      let canvas = document.getElementById('canvas')
      let ctx = canvas.getContext('2d')

      let sCanvas = document.getElementById('sCanvas')
      let sCtx = sCanvas.getContext('2d')

      let w = 600
      let h = 400

      canvas.style.width = w + 'px'
      canvas.style.height = h + 'px'
      canvas.setAttribute('height', h)
      canvas.setAttribute('width', w)

      sCanvas.style.width = w + 'px'
      sCanvas.style.height = h + 'px'
      sCanvas.setAttribute('height', h)
      sCanvas.setAttribute('width', w)

      sCtx.textAlign = 'center'
      sCtx.textBaseline = 'middle'

      let quality = 10

      sCtx.clearRect(0, 0, w, h)
      ctx.clearRect(0, 0, w, h)

      let word = words[this.gRA(0, words.length - 1)]

      sCtx.font = 'normal  900 ' + (w / word.length + 2) + 'px sans-serif'
      sCtx.fillText(word, w / 2, h / 2)

      let possibleSpots = []
      let circles = []

      for (let i = 0; i < sCanvas.width; i += 5) {
        for (let g = 0; g < sCanvas.height; g += 5) {
          let imgData = sCtx.getImageData(i, g, 1, 1).data
          if (imgData[0] > 0 || imgData[1] > 0 || imgData[2] > 0 || imgData[3] > 0) {
            possibleSpots.push({x: i, y: g})
            for (let l = 0; l < 5; l++) {
              for (let h = 0; h < 5; h++) {
                possibleSpots.push({x: i + l, y: g + h})
              }
            }
          }
        }
      }

      sCtx.clearRect(0, 0, w, h)
      let counter = 0

      this.loop(ctx, counter, quality, circles, w, h, possibleSpots)
    }
  },
  mounted () {
    this.initCanvas()
  }
}
</script>

<style scoped>

</style>
