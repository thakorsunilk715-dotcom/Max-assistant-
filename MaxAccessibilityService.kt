package com.max.assistant

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class MaxAccessibilityService : AccessibilityService() {
    companion object { var instance: MaxAccessibilityService? = null }
    override fun onServiceConnected() { instance = this }
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}
    fun scrollForward(): Boolean {
        val root = rootInActiveWindow ?: return false
        return scrollNode(root)
    }
    private fun scrollNode(node: AccessibilityNodeInfo): Boolean {
        if (node.isScrollable && node.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)) return true
        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            if (scrollNode(child)) return true
        }
        return false
    }
    override fun onDestroy() {
        if (instance === this) instance = null
        super.onDestroy()
    }
}
