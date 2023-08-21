import request from '@/utils/request'

// 创建设备信息
export function createDevice(data) {
  return request({
    url: '/radar/device/create',
    method: 'post',
    data: data
  })
}

// 更新设备信息
export function updateDevice(data) {
  return request({
    url: '/radar/device/update',
    method: 'put',
    data: data
  })
}

// 删除设备信息
export function deleteDevice(id) {
  return request({
    url: '/radar/device/delete?id=' + id,
    method: 'delete'
  })
}

// 获得设备信息
export function getDevice(id) {
  return request({
    url: '/radar/device/get?id=' + id,
    method: 'get'
  })
}

// 获得设备信息分页
export function getDevicePage(query) {
  return request({
    url: '/radar/device/page',
    method: 'get',
    params: query
  })
}

// 导出设备信息 Excel
export function exportDeviceExcel(query) {
  return request({
    url: '/radar/device/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}


// 获得设备信息
export function getDeviceStatus(query) {
  return request({
    url: '/radar/device/device-status?ids=' + query.join(),
    method: 'get',
  })
}