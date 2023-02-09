import request from '@/utils/request'

// 创建mqtt主题
export function createMqttTopic(data) {
  return request({
    url: '/system/mqtt-topic/create',
    method: 'post',
    data: data
  })
}

// 更新mqtt主题
export function updateMqttTopic(data) {
  return request({
    url: '/system/mqtt-topic/update',
    method: 'put',
    data: data
  })
}

// 删除mqtt主题
export function deleteMqttTopic(id) {
  return request({
    url: '/system/mqtt-topic/delete?id=' + id,
    method: 'delete'
  })
}

// 获得mqtt主题
export function getMqttTopic(id) {
  return request({
    url: '/system/mqtt-topic/get?id=' + id,
    method: 'get'
  })
}

// 获得mqtt主题分页
export function getMqttTopicPage(query) {
  return request({
    url: '/system/mqtt-topic/page',
    method: 'get',
    params: query
  })
}

// 导出mqtt主题 Excel
export function exportMqttTopicExcel(query) {
  return request({
    url: '/system/mqtt-topic/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
