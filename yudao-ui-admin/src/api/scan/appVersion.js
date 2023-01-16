import request from '@/utils/request'

// 创建应用版本记录
export function createAppVersion(data) {
  return request({
    url: '/scan/app-version/create',
    method: 'post',
    data: data
  })
}

// 更新应用版本记录
export function updateAppVersion(data) {
  return request({
    url: '/scan/app-version/update',
    method: 'put',
    data: data
  })
}

// 删除应用版本记录
export function deleteAppVersion(id) {
  return request({
    url: '/scan/app-version/delete?id=' + id,
    method: 'delete'
  })
}

// 获得应用版本记录
export function getAppVersion(id) {
  return request({
    url: '/scan/app-version/get?id=' + id,
    method: 'get'
  })
}

// 获得应用版本记录分页
export function getAppVersionPage(query) {
  return request({
    url: '/scan/app-version/page',
    method: 'get',
    params: query
  })
}

// 导出应用版本记录 Excel
export function exportAppVersionExcel(query) {
  return request({
    url: '/scan/app-version/export-excel',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}
