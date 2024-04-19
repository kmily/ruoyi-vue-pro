import request from '@/utils/request'

// 创建线下人工充值
export function createOfflineRechange(data) {
  return request({
    url: '/steam/offline-rechange/create',
    method: 'post',
    data: data
  })
}

// 更新线下人工充值
export function updateOfflineRechange(data) {
  return request({
    url: '/steam/offline-rechange/update',
    method: 'put',
    data: data
  })
}

// 删除线下人工充值
export function deleteOfflineRechange(id) {
  return request({
    url: '/steam/offline-rechange/delete?id=' + id,
    method: 'delete'
  })
}

// 获得线下人工充值
export function getOfflineRechange(id) {
  return request({
    url: '/steam/offline-rechange/get?id=' + id,
    method: 'get'
  })
}

// 获得线下人工充值分页
export function getOfflineRechangePage(params) {
  return request({
    url: '/steam/offline-rechange/page',
    method: 'get',
    params
  })
}
// 导出线下人工充值 Excel
export function exportOfflineRechangeExcel(params) {
  return request({
    url: '/steam/offline-rechange/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}