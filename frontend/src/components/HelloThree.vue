<template>
  <div ref="threeContainer" class="gameContainer"></div>
</template>

<script setup>

import * as THREE from 'three';
import {Howl} from 'howler';
import slimeBlock from '@/assets/block/slime_block.png'
import oakLog from '@/assets/block/oak_planks.png'
import skybox from '@/assets/landscape.jpg'
import c418 from "@/assets/sounds/C418  - Sweden - Minecraft Volume Alpha.mp3"
import slime1 from "@/assets/sounds/Slime_big1.ogg"
import slime2 from "@/assets/sounds/Slime_big2.ogg"
import slime3 from "@/assets/sounds/Slime_big3.ogg"
import {PointerLockControls} from "three/addons";
import {GLTFLoader} from 'three/addons/loaders/GLTFLoader.js';
import {colourNameToHex} from "@/utils/colorPicker.ts";
import {onMounted, ref} from "vue";

const threeContainer = ref(null)

onMounted(() => {
  //Variablen für die Bewengung des Spielers werden gesetzt
  let movingFoward, movingBackward, movingLeft, movingRight = false
  const slowMovementSpeed = 10
  const fastMovementSpeed = 20
  let movementSpeed = slowMovementSpeed


  console.log(threeContainer.value);

  //Alle SFX werden hier geladen und gemutet unmute erfolgt im click eventlistener
  function loadSFX() {
    const music = new Howl({
      src: [c418],
      volume: 0.01,
      loop: true,
      onload: () => {
        music.play()
        music.mute(true)
      }
    });
    const bounceSFX1 = new Howl({
      src: [slime1],
      volume: 0.05,
      onload: () => {
        bounceSFX1.play()
        bounceSFX1.mute(true)
      }
    });
    const bounceSFX2 = new Howl({
      src: [slime2],
      volume: 0.05,
      onload: () => {
        bounceSFX2.play()
        bounceSFX2.mute(true)
      }
    });
    const bounceSFX3 = new Howl({
      src: [slime3],
      volume: 0.05,
      onload: () => {
        bounceSFX3.play()
        bounceSFX3.mute(true)
      }
    });
    return [music, bounceSFX1, bounceSFX2, bounceSFX3]
  }

  let sound = loadSFX()

  function createSceneCameraRendererControlls() {

    const scene = new THREE.Scene();

    // Kamera erstellen
    const camera = new THREE.PerspectiveCamera(
      60,
      window.innerWidth / window.innerHeight,
      0.4,
      1000
    );
    camera.position.z = 5;
    camera.position.y = 2;

    const renderer = new THREE.WebGLRenderer();
    renderer.setSize(window.innerWidth, window.innerHeight);
    renderer.shadowMap.enabled = true
    renderer.shadowMap.type = THREE.PCFSoftShadowMap;

    const pointerLockControls = new PointerLockControls(camera, renderer.domElement)
    pointerLockControls.movementSpeed = 5;
    pointerLockControls.lookSpeed = 1;
    pointerLockControls.lookVertical = false
    pointerLockControls.activeLook = true

    return {scene, camera, renderer, pointerLockControls}
  }

  const {scene, camera, renderer, pointerLockControls} = createSceneCameraRendererControlls()

  let clock = new THREE.Clock()

  threeContainer.value.appendChild(renderer.domElement);

  function registerListeners(window, renderer) {
    renderer.domElement.addEventListener('click', (e) => {
      console.log("click")
      sound.forEach((sfx) => sfx.mute(false))
      renderer.domElement.requestPointerLock();
    });

    window.addEventListener('keydown', (e) => {
      switch (e.code) {
        case "ShiftLeft":
          if (movementSpeed === slowMovementSpeed) {
            movementSpeed = fastMovementSpeed
          } else {
            movementSpeed = slowMovementSpeed
          }
          break
        case "KeyW":
          movingFoward = true
          break
        case "KeyA":
          movingLeft = true
          break
        case "KeyS":
          movingBackward = true
          break
        case "KeyD":
          movingRight = true
          break
      }
    });

    window.addEventListener('keyup', (e) => {
      console.log("Losgelasen: " + e.code)
      switch (e.code) {
        case "KeyW":
          movingFoward = false
          break
        case "KeyA":
          movingLeft = false
          break
        case "KeyS":
          movingBackward = false
          break
        case "KeyD":
          movingRight = false
          break
      }
    });
  }

  registerListeners(window, renderer);

  const outerGeometry = new THREE.BoxGeometry();
  const innerGeometry = new THREE.BoxGeometry(0.5, 0.5, 0.5);
  const texture = new THREE.TextureLoader().load(slimeBlock)
  const outerMaterial = new THREE.MeshStandardMaterial({
    map: texture,
    transparent: true,
    opacity: 1.3,
    color: 0x555555
  });
  const innerMaterial = new THREE.MeshStandardMaterial({map: texture, color: 0x555555});
  const outerCube = new THREE.Mesh(outerGeometry, outerMaterial);
  const innerCube = new THREE.Mesh(innerGeometry, innerMaterial);
  outerCube.position.set(0, 2, 0)
  outerCube.castShadow = true
  innerCube.position.set(0, 2, 0)
  innerCube.castShadow = true

  const bodenPlane = new THREE.Plane(new THREE.Vector3(0, 1, 0), -0.5)
  const deckenPlane = new THREE.Plane(new THREE.Vector3(0, 1, 0), -4)
  const wandLinksPlane = new THREE.Plane(new THREE.Vector3(1, 0, 0), -5)
  const wandRechtsPlane = new THREE.Plane(new THREE.Vector3(1, 0, 0), 5)

  const planeHelper = new THREE.PlaneHelper(bodenPlane, 10, 0xffffff)
  const planeHelper2 = new THREE.PlaneHelper(deckenPlane, 10, 0xffffff)
  const planeHelper3 = new THREE.PlaneHelper(wandRechtsPlane, 10, 0xffffff)
  const planeHelper4 = new THREE.PlaneHelper(wandLinksPlane, 10, 0xffffff)


  const modelLoader = new GLTFLoader()

  function loadPlayerModelAt(x, y, z, rotation, modelLoader) {
    modelLoader.load("src/assets/3dModel/minecraft_-_steve/scene.gltf", (objekt) => {
      const model = objekt.scene
      model.scale.set(0.06, 0.06, 0.06)
      model.position.set(x, y, z)
      model.rotation.y = rotation
      scene.add(model)
    })
  }

  loadPlayerModelAt(-6, 1.5, 0, Math.PI / 2, modelLoader)

  //Model hinter der Kamera
  modelLoader.load("src/assets/3dModel/minecraft_-_steve/scene.gltf", (objekt) => {
    const model = objekt.scene
    model.scale.set(0.06, 0.06, 0.06)
    model.position.set(0, -0.8, -0)
    model.rotation.y = Math.PI
    camera.add(model)
  })

  function addCrosshair(camera, color) {
    const crosshairGeometry = new THREE.BoxGeometry(0.015, 0.003, 0.01)
    const material = new THREE.MeshBasicMaterial({color: color})
    const crosshairWidth = new THREE.Mesh(crosshairGeometry, material)
    const crosshairHight = new THREE.Mesh(crosshairGeometry, material)
    camera.updateMatrixWorld();
    crosshairWidth.position.set(0, 0, -0.5)
    crosshairHight.position.set(0, 0, -0.5)
    crosshairHight.rotateZ(Math.PI / 2)
    camera.add(crosshairHight)
    camera.add(crosshairWidth)
  }

  addCrosshair(camera, colourNameToHex("dodgerblue"));

  const pointLight = new THREE.PointLight(0xffffff, 70)
  pointLight.position.set(7, 6.5, -2)
  pointLight.castShadow = true

  const ambientLight = new THREE.AmbientLight(0xffffff, 6)
  ambientLight.position.set(0, 0, 10)


  const pointLightHelper = new THREE.PointLightHelper(pointLight, 0.1, 0xffffff)

  scene.add(pointLight, pointLightHelper, ambientLight, outerCube, innerCube, camera)
  //plainhelper anzeiggen (mit Kommentar ein/aus schalten)
  scene.add(planeHelper, planeHelper2, planeHelper3, planeHelper4)
  scene.background = new THREE.TextureLoader().load(skybox)

  function cameraPositionBewegen(delta) {
    let cameraViewDirection = new THREE.Vector3()
    camera.getWorldDirection(cameraViewDirection)
    const yPlaneVector = new THREE.Vector3(0, 1, 0)
    if (movingFoward || movingBackward || movingLeft || movingRight) {
      if (movingFoward) {
        console.log("movingFoward")
        if (movingRight) {
          console.log("and movingRight")
          camera.position.addScaledVector(cameraViewDirection.applyAxisAngle(yPlaneVector, 7 * Math.PI / 4), movementSpeed * delta)
        } else if (movingLeft) {
          camera.position.addScaledVector(cameraViewDirection.applyAxisAngle(yPlaneVector, Math.PI / 4), movementSpeed * delta)
        } else if (movingBackward) {
          //foward und backward canceln sich
        } else {
          camera.position.addScaledVector(cameraViewDirection.applyAxisAngle(yPlaneVector, 2 * Math.PI), movementSpeed * delta)
        }
      } else if (movingBackward) {
        if (movingRight) {
          camera.position.addScaledVector(cameraViewDirection.applyAxisAngle(yPlaneVector, 5 * Math.PI / 4), movementSpeed * delta)
        } else if (movingLeft) {
          camera.position.addScaledVector(cameraViewDirection.applyAxisAngle(yPlaneVector, 3 * Math.PI / 4), movementSpeed * delta)
        } else {
          camera.position.addScaledVector(cameraViewDirection.applyAxisAngle(yPlaneVector, Math.PI), movementSpeed * delta)
        }
      } else if (movingRight) {
        if (movingLeft) {
          //right und left canceln sich
        } else {
          camera.position.addScaledVector(cameraViewDirection.applyAxisAngle(yPlaneVector, 3 * Math.PI / 2), movementSpeed * delta)
        }
      } else if (movingLeft) {
        camera.position.addScaledVector(cameraViewDirection.applyAxisAngle(yPlaneVector, Math.PI / 2), movementSpeed * delta)
      }
      camera.position.y = 2
    }
  }

  function slimeBlockBounce(delta) {
    const boundingBoxOuterCube = new THREE.Box3(new THREE.Vector3(), new THREE.Vector3());
    boundingBoxOuterCube.setFromObject(outerCube)


    if (boundingBoxOuterCube.intersectsPlane(wandRechtsPlane) || boundingBoxOuterCube.intersectsPlane(wandLinksPlane)) {
      directionX *= -1; // Richtung umkehren
      outerCube.position.x += 0.05 * directionX; //Box clipt mamchmal in die Wand deshalb wird sie nochmal weg geschoben
      innerCube.position.x += 0.05 * directionX;
      sound[2].play()
    }

    if (boundingBoxOuterCube.intersectsPlane(bodenPlane) || boundingBoxOuterCube.intersectsPlane(deckenPlane)) {
      directionY *= -1; // Richtung umkehren
      outerCube.position.y += 0.05 * directionY; //Box clipt mamchmal in den boden/decke deshalb wird sie nochmal weg geschoben
      innerCube.position.y += 0.05 * directionY;
      sound[1].play()
    }

    outerCube.position.x += 1 * delta * directionX;
    outerCube.position.y += 1 * delta * directionY;

    innerCube.position.x += 1 * delta * directionX;
    innerCube.position.y += 1 * delta * directionY;

    outerCube.rotation.x += 0.5 * delta;
    outerCube.rotation.y += 0.2 * delta;

    innerCube.rotation.x += 0.5 * delta;
    innerCube.rotation.y += 0.2 * delta;
  }

  let directionX = 1;
  let directionY = 1;

  function animate() {
    const delta = clock.getDelta()
    requestAnimationFrame(animate);
    slimeBlockBounce(delta);
    cameraPositionBewegen(delta);
    renderer.render(scene, camera);
  }

  animate();

  function bodenGenerieren() {
    const breite = 200;
    const tiefe = 2;

    const geometry = new THREE.BoxGeometry(1, 1, 1);
    const texture = new THREE.TextureLoader().load(oakLog);
    const material = new THREE.MeshToonMaterial({map: texture, color: 0x555555});

    for (let x = -breite / 2; x < breite / 2; x++) {
      for (let z = -tiefe / 2; z < tiefe / 2; z++) {
        const oakPlankBlock = new THREE.Mesh(geometry, material);
        oakPlankBlock.position.set(x, 0, z);
        oakPlankBlock.receiveShadow = true
        scene.add(oakPlankBlock);
      }
    }
  }

  bodenGenerieren();
});
</script>

<style>
html, body {
  margin: 0;
  padding: 0;
  overflow: hidden;
}

.gameContainer {
  border: solid;
}
</style>
