import request from '@/utils/request'

// 创建类型选择
export function createSelType(data) {
  return request({
    url: '/steam/sel-type/create',
    method: 'post',
    data: data
  })
}

// 更新类型选择
export function updateSelType(data) {
  return request({
    url: '/steam/sel-type/update',
    method: 'put',
    data: data
  })
}

// 删除类型选择
export function deleteSelType(id) {
  return request({
    url: '/steam/sel-type/delete?id=' + id,
    method: 'delete'
  })
}

// 获得类型选择
export function getSelType(id) {
  return request({
    url: '/steam/sel-type/get?id=' + id,
    method: 'get'
  })
}

// 获得类型选择分页
export function getSelTypePage(params) {
  return request({
    url: '/steam/sel-type/page',
    method: 'get',
    params
  })
}
// 导出类型选择 Excel
export function exportSelTypeExcel(params) {
  return request({
    url: '/steam/sel-type/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// ==================== 子表（武器选择） ====================
    // 获得武器选择分页
  export function getSelWeaponPage(params) {
    return request({
      url: '/steam/sel-type/sel-weapon/page',
      method: 'get',
      params
    })
  }
        // 新增武器选择
  export function createSelWeapon(data) {
    return request({
      url: '/steam/sel-type/sel-weapon/create',
      method: 'post',
      data
    })
  }
  // 修改武器选择
  export function updateSelWeapon(data) {
    return request({
      url: '/steam/sel-type/sel-weapon/update',
      method: 'post',
      data
    })
  }
  // 删除武器选择
  export function deleteSelWeapon(id) {
    return request({
      url: '/steam/sel-type/sel-weapon/delete?id=' + id,
      method: 'delete'
    })
  }
  // 获得武器选择
  export function getSelWeapon(id) {
    return request({
      url: '/steam/sel-type/sel-weapon/get?id=' + id,
      method: 'get'
    })
  }