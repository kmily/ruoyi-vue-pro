import request from '@/utils/request'

// 创建设备
export function createDevice(data) {
  return request({
    url: '/scan/device/create',
    method: 'post',
    data: data
  })
}

// 更新设备
export function updateDevice(data) {
  return request({
    url: '/scan/device/update',
    method: 'put',
    data: data
  })
}

// 删除设备
export function deleteDevice(id) {
  return request({
    url: '/scan/device/delete?id=' + id,
    method: 'delete'
  })
}

// 获得设备
export function getDevice(id) {
  return request({
    url: '/scan/device/get?id=' + id,
    method: 'get'
  })
}

// 获得设备分页
export function getDevicePage(query) {
  return request({
    url: '/scan/device/page',
    method: 'get',
    params: query
  })
}

// 导出设备 Excel
export function exportDeviceExcel(query) {
  return request({
    url: '/scan/device/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
