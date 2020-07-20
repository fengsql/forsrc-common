package com.forsrc.common.websocket.chief;

import com.forsrc.common.base.BThread;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
@Slf4j
public class ChiefWebsocket {

  @Value("${websocket.enable:false}")
  private boolean enable;

  // <<----------------------- initialize -----------------------

  public void initialize() {
  }

  // >>----------------------- initialize -----------------------

  // <<----------------------- abstract -----------------------

  // >>----------------------- abstract -----------------------

  // <<----------------------- public -----------------------

  // <<<----------------------- normal -----------------------

  public void start() {
    if (!enable) {
      return;
    }

  }

  public void stop() {
    if (!enable) {
      return;
    }
  }

  // >>>----------------------- normal -----------------------

  // <<<----------------------- tool -----------------------

  // >>>----------------------- tool -----------------------

  // >>----------------------- public -----------------------

  // <<----------------------- protected -----------------------

  // >>----------------------- protected -----------------------

  // <<----------------------- private -----------------------

  // <<<----------------------- normal -----------------------

  // >>>----------------------- normal -----------------------

  // <<<----------------------- tool -----------------------
  private void startThread(BThread bThread, String name) {
    Thread thread = new Thread(bThread);
    thread.start();
    log.info("startThread " + name + " ok.");
  }

  private void stopThread(BThread bThread, String name) {
    if (bThread != null) {
      bThread.stop();
    }
  }

  // >>>----------------------- tool -----------------------

  // >>----------------------- private -----------------------

  // <<<----------------------- get -----------------------

  // >>>----------------------- get -----------------------

  // <<<----------------------- set -----------------------

  // >>>----------------------- set -----------------------

  // <<----------------------- get set -----------------------

  // >>----------------------- get set -----------------------
}